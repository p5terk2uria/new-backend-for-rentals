package productservice.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productservice.bookings.dto.PaymentRequest;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.payment.PaymentService;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.payment.enums.PaymentReason;
import productservice.roleutils.RequireRole;
import productservice.visit.dto.VisitRequest;

@RestController
@RequestMapping("/api/products/request")
@RequiredArgsConstructor
public class RequestVisitController extends BaseController {

    private final RequestVisitService requestVisitService;

    private final PaymentService paymentService;


    @RequireRole("TENANT")
    @PostMapping("/visit")
    public ResponseEntity<ApiResponse<?>> requestVisit(
            @RequestBody VisitRequest request
    ) {
        return ResponseEntity.ok(success("Request submitted",
                requestVisitService.requestVisit(request)));
    }

    @PostMapping("/pay-visit")
    public ResponseEntity<ApiResponse<?>> initiatePayment(@RequestBody PaymentRequest request,
                                                          @RequestParam PaymentReason paymentReason) {
        InitiatePaymentResponse response=  requestVisitService.initiatePayment(request, paymentReason);
        return ResponseEntity.ok(success(response));

    }

    @RequireRole("ADMIN, TENANT")
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
