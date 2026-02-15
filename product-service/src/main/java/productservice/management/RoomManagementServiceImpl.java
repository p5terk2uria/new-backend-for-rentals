package productservice.management;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import productservice.bookings.dto.PaymentRequest;
import productservice.feignclients.authentication.AuthenticationClient;
import productservice.feignclients.authentication.UserData;
import productservice.management.dto.AssignTenantToRoomRequest;
import productservice.management.dto.RoomTenantResponse;
import productservice.management.dto.TenantFilterRequest;
import productservice.payment.PaymentService;
import productservice.payment.dto.DomainRoles;
import productservice.payment.enums.PaymentReason;
import productservice.property.entities.RoomBills;
import productservice.property.repository.BillsRepository;
import productservice.room.PaymentStatus;
import productservice.room.Room;
import productservice.room.RoomRepository;
import productservice.specifications.RoomTenantSpecification;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomManagementServiceImpl implements RoomManagementService {

    private final AuthenticationClient authenticationClient;
    private final RoomRepository roomRepository;
    private final RoomTenantRepository roomTenantRepository;
    private final BillsRepository billsRepository;
    private final PaymentService paymentService;

    @Transactional
    @Override
    public void assignTenantToRoom(AssignTenantToRoomRequest request) {

        UserData user = authenticationClient.getUserById(request.tenantId());

        if (user == null || user.role() != DomainRoles.TENANT) {
            throw new RuntimeException("user not found or user is not a tenant");
        }
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RuntimeException("No room found for ths id"));

        if (!room.isVacant()) {
            throw new RuntimeException("This is room has another user onboarded");
        }

        if (roomTenantRepository.existsByUserIdAndRoomId(user.id(), room.getId())) {
            throw new RuntimeException("User already onboarded to this room");
        }

        RoomTenant roomTenant = RoomTenant.builder()
                .propertyId(room.getProperty().getId())
                .userId(user.id())
                .userName(user.firstName())
                .phoneNumber(user.phoneNumber())
                .email(user.emailAddress())
                .roomId(room.getId())
                .balance(BigDecimal.ZERO)
                .paymentStatus(PaymentStatus.UNPAID)
                .leaseDate(request.leaseDate())
                .active(true)
                .build();
        roomTenantRepository.save(roomTenant);
        room.setVacant(false);
        roomRepository.save(room);

    }


    @Override
    public void vacateTenant(String tenantId, String roomId) {

        RoomTenant roomTenant = roomTenantRepository.findRoomTenantByUserIdAndRoomId(tenantId, roomId)
                .orElseThrow(() -> new RuntimeException("No room tenant room found with this configurations"));

        roomTenant.setActive(false);
        roomTenantRepository.save(roomTenant);

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setVacant(true);
        roomRepository.save(room);

    }

    @Override
    public BigDecimal generateRoomBills(String roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found for this id: " + roomId));

        BigDecimal houseBill = parseBill(room.getHouseBill());
        log.debug("House bill for room {}: {}", roomId, houseBill);

        RoomBills otherBills = billsRepository.findRoomBillsByRoomId(roomId)
                .orElse(RoomBills.builder()
                        .maintenanceBill("0")
                        .trashBill("0")
                        .waterBill("0")
                        .houseBill("0")
                        .otherBills("0")
                        .build());

        BigDecimal total = houseBill
                .add(parseBill(otherBills.getMaintenanceBill()))
                .add(parseBill(otherBills.getTrashBill()))
                .add(parseBill(otherBills.getWaterBill()))
                .add(parseBill(otherBills.getOtherBills()));

        log.debug("Total bills for room {}: {}", roomId, total);

        return total;
    }


    @Transactional
    @Override
    public void payRoomBills(String roomId,String userId, String tenantId, BigDecimal paymentAmount) {

        RoomTenant roomTenant = roomTenantRepository.findRoomTenantByUserIdAndRoomId(tenantId, roomId)
                .orElseThrow(() -> new RuntimeException("No room tenant found for this room and tenant"));

        if (!roomTenant.isActive()) {
            throw new RuntimeException("Tenant is not active in this room");
        }


        BigDecimal totalBills = generateRoomBills(roomId);

        BigDecimal currentBalance = Optional.ofNullable(roomTenant.getBalance())
                .orElse(BigDecimal.ZERO);
        BigDecimal totalOutstanding = currentBalance.add(totalBills);

        if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Payment amount must be greater than zero");
        }

        if (paymentAmount.compareTo(totalOutstanding) > 0) {
            throw new RuntimeException("Payment amount cannot exceed outstanding balance");
        }

        PaymentRequest paymentRequest = new PaymentRequest(
                tenantId,
                roomTenant.getPropertyId(),
                null,
                null,
                roomId,
                "KES",
                paymentAmount.floatValue(),
                roomTenant.getRoomId(),
                "RENT_PAYMENT"
        );

        paymentService.initiatePayment(paymentRequest, PaymentReason.RENT_PAYMENT);

        BigDecimal newBalance = totalOutstanding.subtract(paymentAmount);
        roomTenant.setBalance(newBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            roomTenant.setPaymentStatus(PaymentStatus.PAID);
        } else if (newBalance.compareTo(totalOutstanding) < 0) {
            roomTenant.setPaymentStatus(PaymentStatus.HALF_PAID);
        } else {
            roomTenant.setPaymentStatus(PaymentStatus.UNPAID);
        }
        roomTenant.setOrderTracking("Order" + System.currentTimeMillis());
        roomTenantRepository.save(roomTenant);
    }
    @Override
    public Page<RoomTenantResponse> getFilteredTenants(TenantFilterRequest request, Pageable pageable) {
        var spec = RoomTenantSpecification.searchRoomTenant(request);

        return roomTenantRepository.findAll(spec, pageable)
                .map(this::mapToRoomTenantResponse);
    }

    private RoomTenantResponse mapToRoomTenantResponse(RoomTenant tenant) {

        log.debug("Mapping tenant: {}", tenant.getId());

        Room room = roomRepository.findById(tenant.getRoomId())
                .orElse(null);

        if (room == null) {
            log.warn("Room not found for tenant {}, roomId: {}", tenant.getId(), tenant.getRoomId());
        }

        String roomNo = room != null ? room.getRoomNo() : "N/A";

        BigDecimal totalBills = BigDecimal.ZERO;
        try {
            totalBills = generateRoomBills(tenant.getRoomId());
            log.debug("Total bills for room {}: {}", tenant.getRoomId(), totalBills);
        } catch (Exception e) {
            log.error("Error generating bills for room {}: {}", tenant.getRoomId(), e.getMessage());
            totalBills = BigDecimal.ZERO;
        }

        BigDecimal balance = Optional.ofNullable(tenant.getBalance())
                .orElse(BigDecimal.ZERO);

        BigDecimal totalOutstanding = balance.add(totalBills);

        RoomTenantResponse response = new RoomTenantResponse(
                tenant.getId(),
                tenant.getPropertyId(),
                tenant.getUserId(),
                tenant.getUserName(),
                tenant.getPhoneNumber(),
                tenant.getEmail(),
                tenant.getRoomId(),
                roomNo,
                tenant.getLeaseDate(),
                balance,
                totalBills,
                totalOutstanding,
                tenant.getPaymentStatus(),
                tenant.isActive(),
                tenant.getOrderTracking()
        );

        log.debug("Successfully mapped tenant {} to response", tenant.getId());

        return response;
    }
    private BigDecimal parseBill(String bill) {
        try {
            return Optional.ofNullable(bill)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(BigDecimal::new)
                    .orElse(BigDecimal.ZERO);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }




    @Override
    public void updateRoomBillsPaymentStatus(String roomId, PaymentStatus paymentStatus) {

    }


    @Override
    public void updateRoomStatus(String roomId) {

    }

    @Override
    public Page<RoomTenantResponse> getRoomsWithOverduePayments(Pageable pageable) {
        return null;
    }
}
