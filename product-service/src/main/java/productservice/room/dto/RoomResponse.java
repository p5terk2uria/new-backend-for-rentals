package productservice.room.dto;

import lombok.Builder;
import productservice.property.enums.HouseType;

@Builder(toBuilder = true)
public record RoomResponse(
        String id,

        HouseType roomType,

        String roomNo,

        String propertyName,

        boolean vacant,

        String houseBill,

        String waterBill,

        String trashBill,

        String maintenanceBill,

        String otherBills,

        String roomVideo
) {}
