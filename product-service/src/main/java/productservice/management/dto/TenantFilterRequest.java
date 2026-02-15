package productservice.management.dto;

import productservice.room.PaymentStatus;

public record TenantFilterRequest(
        String propertyId,
        Boolean activeOnly,
        PaymentStatus paymentStatus,
        String userName,
        String email,
        String roomId
) {
}
