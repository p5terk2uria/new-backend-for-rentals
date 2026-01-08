package productservice.payment.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record CallBackResponse (
        String orderTrackingId,

        String merchantReferenceId,

        String orderNotificationType
) {
}
