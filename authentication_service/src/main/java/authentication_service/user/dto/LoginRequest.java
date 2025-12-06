package authentication_service.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = " Request for login request ")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record LoginRequest (

        @Schema(description = "The email of the user who is logging in")
        @NotBlank(message = "Email of the user cannot be null")
        String email,

        @Schema(description = "The correct password of registering user")
        @NotBlank(message = "password be null")
        String password
) {
}
