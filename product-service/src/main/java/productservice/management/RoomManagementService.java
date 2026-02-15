package productservice.management;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import productservice.management.dto.AssignTenantToRoomRequest;
import productservice.management.dto.RoomTenantResponse;
import productservice.management.dto.TenantFilterRequest;
import productservice.room.PaymentStatus;

import java.math.BigDecimal;

public interface RoomManagementService {

    void assignTenantToRoom(AssignTenantToRoomRequest request);

    void vacateTenant(String tenantId, String roomId);


    BigDecimal generateRoomBills(String roomId);

    @Transactional
    void payRoomBills(String roomId, String userId,String tenantId, BigDecimal paymentAmount);

    Page<RoomTenantResponse> getFilteredTenants(TenantFilterRequest request, Pageable pageable);

    void updateRoomBillsPaymentStatus(String roomId, PaymentStatus paymentStatus);

    void updateRoomStatus(String roomId);

    Page<RoomTenantResponse> getRoomsWithOverduePayments(Pageable pageable);
}
