package system.services.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RequestServiceRequest(

        String userId,

        String serviceId,

        String serviceProviderId,

        LocalDate dateRequested,

        BigDecimal budget,

        String location,

        LocalDate expectedDeadline,

        String description


) {
}
