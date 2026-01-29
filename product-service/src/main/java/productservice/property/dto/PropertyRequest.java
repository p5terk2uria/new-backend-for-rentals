package productservice.property.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jdk.jfr.Description;
import productservice.property.enums.AmenityType;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Description("The request to be used when creating a new searchProperty")
public record PropertyRequest(

        @NotBlank(message = "Owner name cannot be blank")
        String ownerName,

        @NotBlank(message = "OwnerIdCannot be null")
        String ownerId,

        @NotBlank(message = "OwnerIdCannot be null")
        String propertyName,

        @NotBlank(message = "Owner email cannot be empty")
        String ownerEmail,

        @NotBlank(message = "The searchProperty location must be disclosed")
        String propertyAddress,

        @NotBlank(message = "The searchProperty location must be disclosed")
        String propertyLocation,

        @NotBlank(message = "HouseDescription type must be specified")
        String houseDescription,

        Set<AmenityType> amenities


) {
}
