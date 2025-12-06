package productservice.mapper;

import org.springframework.stereotype.Component;
import productservice.property.dto.BillsRequest;
import productservice.property.dto.PropertyRequest;
import productservice.property.entities.Property;
import productservice.property.entities.PropertyAmenities;
import productservice.property.entities.PropertyBills;
import productservice.property.enums.AmenityType;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PropertyMapper {

    public Property toPropertyEntity(PropertyRequest request) {
        return Property.builder()
                .ownerName(request.ownerName())
                .ownerId(request.ownerId())
                .propertyName(request.propertyName())
                .ownerEmail(request.ownerEmail())
                .propertyLocation(request.propertyLocation())
                .houseType(request.houseType())
                .houseDescription(request.houseDescription())
                .build();
    }

    public Set<PropertyAmenities> toPropertyAmenities(Set<AmenityType> amenityTypes, Property property) {
        return amenityTypes.stream()
                .map(type -> PropertyAmenities.builder()
                        .amenityType(type)
                        .property(property)
                        .build())
                .collect(Collectors.toSet());
    }

    public Set<PropertyBills> toPropertyBills(Set<BillsRequest> requests, Property property) {
        return requests.stream()
                .map(bill -> PropertyBills.builder()
                        .property(property)
                        .houseBill(bill.houseBill())
                        .maintenanceBill(bill.maintenanceBill())
                        .waterBill(bill.waterBill())
                        .trashBill(bill.trashBill())
                        .otherBills(bill.otherBills())
                        .build())
                .collect(Collectors.toSet());

    }

}
