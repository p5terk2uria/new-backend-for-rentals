package authentication_service.user;

import java.util.HashMap;
import java.util.Map;

import authentication_service.user.dto.LoginRequest;
import authentication_service.user.dto.LoginResponse;
import authentication_service.user.dto.RegisterRequest;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequest request) {
        userManagementService.registerUser(request);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userManagementService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

}
