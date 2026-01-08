package productservice.bookings.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SubmitResponse(

        @JsonProperty("order_tracking_id")
        String orderTrackingId,

        @JsonProperty("merchant_reference")
        String merchantReference,

        @JsonProperty("redirect_url")
        String redirectUrl,

        Object error,

        String status,

        String message

) {}
