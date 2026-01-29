package productservice.mapper;

import org.springframework.stereotype.Component;
import productservice.property.dto.PropertyRequest;
import productservice.property.dto.PropertyResponse;
import productservice.property.entities.Property;
import productservice.property.entities.PropertyAmenities;
import productservice.property.entities.RoomBills;
import productservice.property.enums.AmenityType;
import productservice.room.Room;
import productservice.room.dto.RoomResponse;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class PropertyMapper {

    public Property toEntity(PropertyRequest request) {
        return Property.builder()
                .ownerId(request.ownerId())
                .ownerName(request.ownerName())
                .ownerEmail(request.ownerEmail())
                .propertyName(request.propertyName())
                .propertyAddress(request.propertyAddress())
                .propertyLocation(request.propertyLocation())
                .houseDescription(request.houseDescription())
                .build();
    }

    public RoomResponse toRoomResponse(Room room, RoomBills bills) {
        return new RoomResponse(
                room.getId(),
                room.getHouseType(),
                room.getRoomNo(),
                null,
                null,
                room.isVacant(),
                bills != null ? bills.getHouseBill() : null,
                bills != null ? bills.getWaterBill() : null,
                bills != null ? bills.getTrashBill() : null,
                bills != null ? bills.getMaintenanceBill() : null,
                bills != null ? bills.getOtherBills() : null,
                room.getVideoUrl()
        );
    }

    public RoomResponse toRoomResponse(Room room) {
        return toRoomResponse(room, room.getRoomBills());
    }

    public PropertyResponse toResponse(Property property, Set<AmenityType> amenities, Set<RoomResponse> rooms) {
        return new PropertyResponse(
                property.getId(),
                property.getPropertyName(),
                property.getPropertyLocation(),
                property.getVideoLink(),
                property.getHouseDescription(),
                amenities,
                rooms
        );
    }

    public Set<PropertyAmenities> toPropertyAmenities(Set<AmenityType> amenityTypes, Property property) {
        return amenityTypes.stream()
                .map(type -> PropertyAmenities.builder()
                        .amenityType(type)
                        .property(property)
                        .build())
                .collect(Collectors.toSet());
    }

}
