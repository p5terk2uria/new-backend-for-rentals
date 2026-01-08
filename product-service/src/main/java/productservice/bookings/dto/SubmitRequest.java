package productservice.bookings.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import productservice.payment.enums.PaymentReason;

@Builder(toBuilder = true)
public record SubmitRequest(

        String id,

        String currency,

        Float amount,

        String description,

        String referenceId,

        String paymentReason,

        @JsonProperty("redirect_mode")
        String redirectMode,

        @JsonProperty("callback_url")
        String callbackUrl,

        @JsonProperty("cancellation_url")
        String cancellationUrl,

        @JsonProperty("notification_id")
        String notificationId,

        @JsonProperty("billing_address")
        CustomerAddress billingAddress


) {
}
