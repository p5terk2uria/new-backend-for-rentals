package productservice.property.dto;

import productservice.property.enums.AmenityType;
import productservice.property.enums.HouseType;
import java.math.BigDecimal;

public record PropertySearchRequest (

        String ownerId,

        String ownerName,

        String propertyName,

        String propertyLocation,

        HouseType houseType,

        AmenityType amenityType,

        BigDecimal minMonthlyBill,

        BigDecimal maxMonthlyBill,

        BigDecimal minWaterBill,

        BigDecimal maxWaterBill,

        BigDecimal minTrashBill,

        BigDecimal maxTrashBill,

        BigDecimal minMaintenanceBill,

        BigDecimal maxMaintenanceBill,

        BigDecimal minOtherBills,

        BigDecimal maxOtherBills
) {
}
