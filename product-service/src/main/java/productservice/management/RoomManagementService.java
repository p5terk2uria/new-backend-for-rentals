package productservice.management;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import productservice.management.dto.AssignTenantToRoomRequest;
import productservice.management.dto.FilteredRequest;
import productservice.management.dto.RoomTenantResponse;
import productservice.room.PaymentStatus;

public interface RoomManagementService {

    void assignTenantToRoom(AssignTenantToRoomRequest request);

    void vacateTenant(String tenantId, String roomId);

    Page<RoomTenantResponse> getFilteredRooms(FilteredRequest request, Pageable pageable);

    void generateRoomBills(String roomId);

    void payRoomBills(String roomId, String tenantId);

    void updateRoomBillsPaymentStatus(String roomId, PaymentStatus paymentStatus);

    void updateRoomStatus(String roomId);

    Page<RoomTenantResponse> getRoomsWithOverduePayments(Pageable pageable);
}
