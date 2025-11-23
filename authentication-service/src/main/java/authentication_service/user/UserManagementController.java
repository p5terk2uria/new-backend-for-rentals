package authentication_service.user;


import authentication_service.user.dto.LoginRequest;
import authentication_service.user.dto.LoginResponse;
import authentication_service.user.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@Tag(name = "User Management Controller")
public class UserManagementController {

    private final UserManagementService userManagementService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        userManagementService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest request) {
        LoginResponse response = userManagementService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
