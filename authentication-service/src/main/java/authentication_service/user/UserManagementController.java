package authentication_service.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import authentication_service.user.config.ApiResponse;
import authentication_service.user.config.BaseController;
import authentication_service.user.dto.*;
import authentication_service.user.enums.DomainRoles;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@Tag(name = "User Management Controller")
public class UserManagementController extends BaseController {

    private final UserManagementService userManagementService;

    @PostMapping("/register")
    public ResponseEntity <ApiResponse<?>> registerUser(@RequestBody RegisterRequest request) {
        String response = userManagementService.registerUser(request);
        return ResponseEntity.ok(success(response));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userManagementService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/user-by-id")
    public ResponseEntity<UserData> getUserDataById(@RequestParam String userId){
        UserData response = userManagementService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @Hidden
    @GetMapping("/feign/user-by-id")
    public ResponseEntity<UserData> getUserData(@RequestParam String userId){
        UserData response = userManagementService.findById(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @Hidden
    @PostMapping("/validate-token")
    public ResponseEntity<ValidationResponse> validateToken (
            @RequestHeader("Authorization") String authHeader) {
        ValidationResponse response = userManagementService.validateToken(authHeader);
        if(response.valid()) {
            return ResponseEntity.ok(response);
        } else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @Hidden
    @GetMapping("/get-users-by-role")
    public ResponseEntity<List<UserData>> getUsersByRoles(
            @RequestParam DomainRoles role)
    {
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.getUsersByRoles(role));
    }


}
