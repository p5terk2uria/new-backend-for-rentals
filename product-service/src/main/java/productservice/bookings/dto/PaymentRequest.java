package productservice.bookings.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentRequest(

        String userId,

        String visitId,

        String bookRoomId,

        String currency,

        Float amount,

        String description

) {

}
