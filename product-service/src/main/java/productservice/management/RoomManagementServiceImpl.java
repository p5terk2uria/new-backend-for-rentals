package productservice.management;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import productservice.feignclients.authentication.AuthenticationClient;
import productservice.feignclients.authentication.UserData;
import productservice.management.dto.AssignTenantToRoomRequest;
import productservice.management.dto.FilteredRequest;
import productservice.management.dto.RoomTenantResponse;
import productservice.payment.dto.DomainRoles;
import productservice.room.PaymentStatus;
import productservice.room.Room;
import productservice.room.RoomRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RoomManagementServiceImpl implements RoomManagementService {

    private final AuthenticationClient authenticationClient;
    private final RoomRepository roomRepository;
    private final RoomTenantRepository roomTenantRepository;

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
        room.setVacant(true);
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
    public Page<RoomTenantResponse> getFilteredRooms(FilteredRequest request, Pageable pageable) {
        return null;
    }


    @Override
    public void generateRoomBills(String roomId) {

    }


    @Override
    public void payRoomBills(String roomId, String tenantId) {

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
