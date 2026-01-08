package productservice.bookings.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterIPNResponse(
        String url,
        @JsonProperty("created_date")
        String createdDate,
        @JsonProperty("ipn_id")
        String ipnId,
        @JsonProperty("ipn_notification_type_description")
        String ipnNotificationTypeDescription,
        @JsonProperty("ipn_status")
        Integer ipnStatus,
        @JsonProperty("ipn_status_description")
        String ipnStatusDescription,
        Object error,
        String status
) {}
