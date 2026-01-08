package productservice.visits;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.roleutils.CurrentUser;
import productservice.roleutils.RequireRole;
import productservice.roleutils.UserInfo;
import productservice.visits.dto.VisitRequest;

@RestController
@RequestMapping("/api/products/request")
@RequiredArgsConstructor
public class RequestVisitController extends BaseController {

    private final RequestVisitService requestVisitService;


    //@RequireRole("TENANT")
    @PostMapping("/visit")
    public ResponseEntity<ApiResponse<?>> requestVisit(
            @RequestBody VisitRequest request
            //@CurrentUser UserInfo user
    ) {
        requestVisitService.requestVisit(request);
        return ResponseEntity.ok(success("Request submitted"));
    }

    //@RequireRole("ADMIN, TENANT")
    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse<?>> updateVisitStatus(
            @RequestParam String userId,
            @RequestParam String visitId,
            @RequestParam RequestVisit.RequestStatus currentStatus,
            @RequestParam RequestVisit.RequestStatus desiredStatus
    ) {

        requestVisitService.updateVisitStatus(userId, visitId, currentStatus, desiredStatus);
        return ResponseEntity.ok(success("status updated successfully"));
    }


}
