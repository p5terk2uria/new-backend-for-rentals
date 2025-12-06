package productservice.property.dto;

import productservice.property.enums.AmenityType;
import productservice.property.enums.HouseType;

import java.util.Set;

public record PropertyResponse (

        String id,

        String ownerId,

        String ownerName,

        String propertyName,

        String ownerEmail,

        String propertyLocation,

        HouseType houseType,

        String videoLink,

        Set<BillsRequest> bills,

        Set<AmenityType> amenities
        



) {
}
