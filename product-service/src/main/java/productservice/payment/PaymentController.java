package productservice.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import productservice.bookings.dto.PaymentRequest;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.payment.dto.CallBackResponse;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.payment.enums.PaymentReason;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/payment")
@Slf4j
public class PaymentController extends BaseController {

    private final PaymentService paymentService;

    @PostMapping("/callback")
    public ResponseEntity<ApiResponse<?>> saveCallBack(@RequestBody(required = false) Map<String, String> payload) {

        if (payload == null || payload.isEmpty()) {
            return ResponseEntity.badRequest().body(failure("Missing parameters"));
        }

        String orderTrackingId = payload.get("OrderTrackingId");
        String merchantReference = payload.get("OrderMerchantReference");
        String notificationType = payload.get("OrderNotificationType");

        if (orderTrackingId == null || merchantReference == null || notificationType == null) {
            log.warn("Missing parameters in callback!");
            return ResponseEntity.badRequest().body(failure("Missing parameters"));
        }

        CallBackResponse response = CallBackResponse.builder()
                .orderTrackingId(orderTrackingId)
                .merchantReferenceId(merchantReference)
                .orderNotificationType(notificationType)
                .build();

        log.info("Processed callback into object: {}", response);

        var results = paymentService.recordRequestVisitCallBack(response);

        return ResponseEntity.ok(success(results));
    }

    @PostMapping("/pay-serviceProvider-OnBoarding-fee")
    public ResponseEntity<ApiResponse<?>> payServiceProviderOnboardingFee(@RequestBody PaymentRequest request) {

         var response = paymentService.initiateCommissionPayment(request,PaymentReason.ONBOARDING_COMMISSION);
         return ResponseEntity.ok(success(response));
    }



}
