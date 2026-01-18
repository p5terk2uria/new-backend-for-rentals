package authentication_service.user.dto;

import authentication_service.user.location.LocationDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public record LocationDetailsRequest (

        String country,

        String countryCode,

        String state,

        @NotNull(message = "city cannot be null")
        String city,

        String postalCode,

        String zipCode

) {
    public static LocationDetails toLocationEntity(LocationDetailsRequest request) {
        return LocationDetails.builder()
                .country(request.country)
                .countryCode(request.countryCode)
                .state(request.state)
                .postalCode(request.postalCode)
                .zipCode(request.zipCode)
                .build();
    }
}
