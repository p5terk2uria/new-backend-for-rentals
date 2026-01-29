package productservice.feignclients.service;

import lombok.Builder;
import java.math.BigDecimal;

@Builder(toBuilder = true)
public record OrderServiceResponse(

        String id,

        String userId,

        String serviceName,

        String serviceProviderId,

        String dateRequested,

        String expectedDeadline,

        String orderId,

        BigDecimal budget,

        String description,

        String location,

        OrderStatus orderStatus


) {

}
