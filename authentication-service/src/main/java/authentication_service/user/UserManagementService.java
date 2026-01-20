package authentication_service.user;

import authentication_service.user.dto.*;
import authentication_service.user.enums.DomainRoles;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserManagementService {

    void registerUser(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    UserData findById(String userId);

    ValidationResponse validateToken(String authHeader);

    List<UserData> getUsersByRoles(DomainRoles roles);


}
