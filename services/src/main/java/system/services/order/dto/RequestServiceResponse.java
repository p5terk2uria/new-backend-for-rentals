package system.services.order.dto;

import system.services.order.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RequestServiceResponse (

        String id,

        String userId,

        String serviceId,

        String serviceProviderId,

        LocalDate dateRequested,

        LocalDate expectedDeadline,

        String orderId,

        BigDecimal budget,

        String description,

        OrderStatus orderStatus
) {
}
