package productservice.management;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.management.dto.AssignTenantToRoomRequest;

@RestController
@RequestMapping("/api/products/management")
@RequiredArgsConstructor
public class RoomManagementController extends BaseController {

    private final RoomManagementService roomManagementService;

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




}
