package productservice.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productservice.bookings.dto.PaymentRequest;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.payment.PaymentService;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.payment.enums.PaymentReason;
import productservice.roleutils.RequireRole;
import productservice.visit.dto.RequestVisitSearchRequest;
import productservice.visit.dto.VisitRequest;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/products/request")
@RequiredArgsConstructor
public class RequestVisitController extends BaseController {

    private final RequestVisitService requestVisitService;

    private final PaymentService paymentService;


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
        InitiatePaymentResponse response = requestVisitService.initiatePayment(request, paymentReason);
        return ResponseEntity.ok(success(response));

    }

    @GetMapping("/get-visit-entity-by-id")
    public ResponseEntity<ApiResponse<?>> getVisitEntityById(
            @RequestParam String visitId
    ) {
        return ResponseEntity.ok(success(
                "success", requestVisitService.getVisitById(visitId)));
    }

    @PutMapping("/update-status")
    public ResponseEntity<ApiResponse<?>> updateVisitStatus(
            @RequestParam String userId,
            @RequestParam String visitId,
            @RequestParam RequestVisit.RequestStatus desiredStatus
    ) {

        requestVisitService.updateVisitStatus(userId, visitId, desiredStatus);
        return ResponseEntity.ok(success("status updated successfully"));
    }

    @GetMapping("/search")
    public ApiResponse<Page<?>> searchVisits(
            @RequestParam(required = false) String visitId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String orderTrackingId,
            @RequestParam(required = false) String tenantName,
            @RequestParam(required = false) RequestVisit.RequestStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate visitingDateFrom,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate visitingDateTo,
            @RequestParam(required = false) String roomId,
            @RequestParam(required = false) String propertyId,
            Pageable pageable
    ) {
        var request = new RequestVisitSearchRequest(
                visitId,
                userId,
                orderTrackingId,
                tenantName,
                status,
                visitingDateFrom,
                visitingDateTo,
                roomId,
                propertyId
        );

        Page<?> responsePage =
                requestVisitService.searchRequests(request, pageable);

        return new ApiResponse<>(true, "Fetched visit requests", responsePage);
    }


}
