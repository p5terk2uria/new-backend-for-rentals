package system.services.order.dto;

import lombok.Builder;
import system.services.order.enums.OrderStatus;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record AdminOrderResponse (
        String id,

        String userId,

        String serviceName,

        String serviceProviderId,

        String dateRequested,

        String expectedDeadline,

        String orderTrackingId,

        BigDecimal budget,

        String description,

        OrderStatus orderStatus
) {
}
