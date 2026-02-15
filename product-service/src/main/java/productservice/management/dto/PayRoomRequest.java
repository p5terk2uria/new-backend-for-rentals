package productservice.management.dto;

import java.math.BigDecimal;

public record PayRoomRequest(
        String roomId,
        String userId,
        String tenantId,
        BigDecimal paymentAmount
) {
}
