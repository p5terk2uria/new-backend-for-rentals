package productservice.management.dto;

import productservice.room.PaymentStatus;

public record FilteredRequest (

        String tenantId,

        String roomId,

        String tenantName,

        PaymentStatus paymentStatus,

        String propertyId



) {
}
