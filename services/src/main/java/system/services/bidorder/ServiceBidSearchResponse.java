package system.services.bidorder;

import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public record ServiceBidSearchResponse (

        String id,

        String orderId,

        String serviceProviderId,

        String message,

        BidStatus bidStatus,

        LocalDate bidedAt

){
}
