package productservice.property.dto;

import productservice.property.enums.AmenityType;
import productservice.room.dto.RoomResponse;

import java.util.Set;

public record PropertyResponse(
        String id,

        String propertyName,

        String propertyLocation,

        String videoLink,

        Set<AmenityType> amenities,

        Set<RoomResponse> rooms
) {}
