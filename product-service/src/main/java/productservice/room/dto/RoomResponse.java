package productservice.room.dto;

import productservice.property.enums.HouseType;


public record RoomResponse(
        String id,

        HouseType roomType,

        String roomNo,

        boolean vacant,

        String houseBill,

        String waterBill,

        String trashBill,

        String maintenanceBill,

        String otherBills,

        String roomVideo
) {}
