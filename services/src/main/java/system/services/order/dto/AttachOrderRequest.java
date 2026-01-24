package system.services.order.dto;

public record AttachOrderRequest (

        String orderId,

        String serviceProvideId
) {
}
