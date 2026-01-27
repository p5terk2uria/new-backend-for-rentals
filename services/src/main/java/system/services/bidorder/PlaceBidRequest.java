package system.services.bidorder;

import java.math.BigDecimal;

public record PlaceBidRequest (
        String orderId,
        String serviceProviderId,
        BigDecimal amount,
        String message
){
}
