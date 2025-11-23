package authentication_service.user;

import authentication_service.user.config.SecurityConfig;
import authentication_service.user.dto.LoginRequest;
import authentication_service.user.dto.LoginResponse;
import authentication_service.user.dto.RegisterRequest;
import authentication_service.user.enums.UserManagementRepository;
import de.mkammerer.argon2.Argon2Factory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserManagementImpl implements UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final SecurityConfig securityConfig;

    /**
     * @param request
     */
    @Override
    public void registerUser(RegisterRequest request) {

        User newUser = RegisterRequest.toUserTable(request);

        String hashedPassword = hashPassword(request.password());

        newUser.setPassword(hashedPassword);

        userManagementRepository.save(newUser);

    }

    /**
     * @param request
     * @return
     */
    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userManagementRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        var argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2d);
        boolean passwordMatches = argon2.verify(user.getPassword(), request.password());
        if (!passwordMatches) {
            throw new RuntimeException("Invalid password or email");
        }
        String token = securityConfig.generateJwtToken(user);

        return new LoginResponse(
                user.getEmail(),
                token,
                user.getRole()
        );

    }


    private String hashPassword(String password) {

        var argon2 = de.mkammerer.argon2.Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2d);

        return argon2.hash(3, 65536, 1, password);

    }
}
