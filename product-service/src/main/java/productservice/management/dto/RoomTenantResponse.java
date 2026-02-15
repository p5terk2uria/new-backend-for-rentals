package productservice.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import productservice.property.enums.HouseType;
import productservice.room.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RoomTenantResponse (

        String id,
        String propertyId,
        String userId,
        String userName,
        String phoneNumber,
        String email,
        String roomId,
        String roomNo,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate leaseDate,
        BigDecimal balance,
        BigDecimal totalBills,
        BigDecimal totalOutstanding,
        PaymentStatus paymentStatus,
        boolean active,
        String orderTracking

        ) {
}
