package system.services.bidorder;

import lombok.Builder;

@Builder(toBuilder = true)
public record ServiceBidSearchRequest(

        String orderId,

        String serviceProviderId,

        BidStatus status

) {
}
