package productservice.property.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Description;
import productservice.property.entities.PropertyAmenities;
import productservice.property.entities.PropertyBills;
import productservice.property.enums.AmenityType;
import productservice.property.enums.HouseType;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Description("The request to be used when creating a new property")
public record PropertyRequest(

        @NotBlank(message = "Owner name cannot be blank")
        String ownerName,

        @NotBlank(message = "OwnerIdCannot be null")
        String ownerId,

        String propertyName,

        @NotBlank(message = "Owner email cannot be empty")
        String ownerEmail,

        @NotBlank(message = "The property location must be disclosed")
        String propertyLocation,

        @NotBlank(message = "House type must be specified")
        HouseType houseType,

        Set<AmenityType> amenities,

        Set<BillsRequest> bills

) {
}
