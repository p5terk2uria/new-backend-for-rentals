package authentication_service.user;

import authentication_service.user.dto.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserManagementService {

    void registerUser(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserData findById(String userId);

    ValidationResponse validateToken(String authHeader);


}
