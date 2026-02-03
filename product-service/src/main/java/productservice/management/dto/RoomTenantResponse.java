package productservice.management.dto;

import productservice.property.enums.HouseType;
import productservice.room.PaymentStatus;

import java.math.BigDecimal;

public record RoomTenantResponse (

        HouseType houseType,

        String roomNo,

        String roomId,

        String propertyName,

        String tenantName,

        String userId,

        String tenantPhoneNumber,

        String email,

        String totalRoomBill,

        BigDecimal balance,

        PaymentStatus paymentStatus


        ) {
}
