package system.services.order.dto;

import system.services.order.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderSearchRequest (

        String serviceId,

        String serviceName,

        OrderStatus status,

        BigDecimal minBudget,

        BigDecimal maxBudget,

        LocalDate fromDate,

        LocalDate toDate

) {
}
