package productservice.bookings;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterIPNRequest(
        String url,
        @JsonProperty("ipn_notification_type")
        String ipnNotificationType
) {}
