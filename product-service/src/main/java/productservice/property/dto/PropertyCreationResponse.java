package productservice.property.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record PropertyCreationResponse (
        String propertyId,
        String propertyName
){
}
