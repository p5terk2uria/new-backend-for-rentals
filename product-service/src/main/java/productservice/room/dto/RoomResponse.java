package productservice.room.dto;

import productservice.property.enums.HouseType;
import java.util.Set;

public record RoomResponse(
        String id,

        HouseType roomType,

        boolean vacant,

        String houseBill,

        String waterBill,

        String trashBill,

        String maintenanceBill,

        String otherBills,

        Set<String> roomVideo
) {}
