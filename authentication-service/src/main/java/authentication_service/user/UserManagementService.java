package authentication_service.user;

import authentication_service.user.dto.LoginRequest;
import authentication_service.user.dto.LoginResponse;
import authentication_service.user.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserManagementService {

    public void registerUser(RegisterRequest request);

    public LoginResponse login (LoginRequest request);
}
