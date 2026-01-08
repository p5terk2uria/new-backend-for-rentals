package authentication_service.user.dto;

import authentication_service.user.User;
import authentication_service.user.enums.DomainRoles;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "Request for user registration")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record RegisterRequest(

        @Schema(description = "The first name of the registering user")
        @NotBlank(message = "first name cannot be null")
        String firstName,

        @Schema(description = "The last name of the registering user")
        @NotBlank(message = "last name cannot be null")
        String lastName,

        @Schema(description = "The email of the registering user")
        @NotBlank(message = "email cannot be null")
        String email,

        @Schema(description = "The phone number of the registering user")
        @NotNull(message = "phone number cannot be null")
        String phoneNumber,

        @Schema(description = "Domain role of the registering user")
        @NotNull(message = "Domain role must be specified")
        DomainRoles role,

        @Schema(description = "the registration password must be specified")
        String password,

        LocationDetailsRequest locationDetails
) {
    public static User toUserTable(RegisterRequest registerRequest) {
        return User.builder()
                .email(registerRequest.email)
                .phoneNumber(registerRequest.phoneNumber)
                .firstName(registerRequest.firstName)
                .lastName(registerRequest.lastName)
                .role(registerRequest.role)
                .build();
    }
}
