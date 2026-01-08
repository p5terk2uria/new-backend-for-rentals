package productservice.bookings.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder(toBuilder = true)
public record CustomerAddress(

        @JsonProperty("phone_number")
        String phoneNumber,

        @JsonProperty("email_address")
        String emailAddress,

        @JsonProperty("country_code")
        String countryCode,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("middle_name")
        String middleName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("line_1")
        String line1,

        @JsonProperty("line_2")
        String line2,

        String city,

        String state,

        @JsonProperty("postal_code")
        String postalCode,

        @JsonProperty("zip_code")
        String zipCode
) {}
