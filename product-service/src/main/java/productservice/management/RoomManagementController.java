package productservice.management;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.management.dto.AssignTenantToRoomRequest;
import productservice.management.dto.PayRoomRequest;
import productservice.management.dto.RoomTenantResponse;
import productservice.management.dto.TenantFilterRequest;
import productservice.room.PaymentStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products/management")
@RequiredArgsConstructor
@Slf4j
public class RoomManagementController extends BaseController {

    private final RoomManagementService roomManagementService;

    @PersistenceContext
    private EntityManager entityManager;


    @PostMapping("/assign-tenant-to-room")
    public ResponseEntity<ApiResponse<?>> assignTenantToRoom(@RequestBody AssignTenantToRoomRequest request) {

        roomManagementService.assignTenantToRoom(request);
        return ResponseEntity.ok(success("Onboarded successfully"));
    }

    @PostMapping("/vacate-tenant-from-room")
    public ResponseEntity<ApiResponse<?>> vacateUserFromRoom(@RequestParam String tenantId,
                                                             @RequestParam String roomId){
        roomManagementService.vacateTenant(tenantId,roomId);
        return ResponseEntity.ok(success("vacation executed successfully"));
    }

    @PostMapping("/pay-room-bills")
    public ResponseEntity<ApiResponse<?>> payRoomBills(@RequestBody PayRoomRequest request){
        roomManagementService. payRoomBills(request.roomId(), request.userId(),
                request.tenantId(),request.paymentAmount());
        return ResponseEntity.ok(success("vacation executed successfully"));
    }

    @GetMapping("/get-roomBills")
    public ResponseEntity<ApiResponse<?>> getRoomBills(@RequestParam String roomId){
        roomManagementService.generateRoomBills(roomId);
        return ResponseEntity.ok(success("success"));
    }

    @GetMapping("/search-properties")
    public ResponseEntity<ApiResponse<Page<?>>> getFilteredTenants(

            @RequestParam(required = false) String propertyId,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String roomId,
            Pageable pageable
    ) {
        TenantFilterRequest filterRequest = new TenantFilterRequest(
                propertyId, activeOnly,
                paymentStatus, userName,
                email, roomId
        );
        var tenants = roomManagementService.getFilteredTenants(filterRequest,
                pageable
        );
        return ResponseEntity.ok(success(tenants));
    }






}
