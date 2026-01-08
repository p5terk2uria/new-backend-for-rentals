package productservice.payment.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record InitiatePaymentResponse(
        String orderTrackingId,
        String merchantReference,
        String redirectUrl,
        int error,
        String message
) {
}
