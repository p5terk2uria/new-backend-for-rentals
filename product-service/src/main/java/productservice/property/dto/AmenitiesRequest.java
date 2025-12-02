package productservice.property.dto;

import jdk.jfr.Description;
import productservice.property.enums.AmenityType;

@Description("A request to save the passed amenities")
public record AmenitiesRequest (

        AmenityType amenityType
){
}
