package authentication_service.user;

import authentication_service.user.config.SecurityConfig;
import authentication_service.user.dto.*;
import authentication_service.user.enums.UserManagementRepository;
import authentication_service.user.location.LocationDetails;
import authentication_service.user.location.LocationRepository;
import de.mkammerer.argon2.Argon2Factory;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserManagementImpl implements UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final SecurityConfig securityConfig;
    private final LocationRepository locationRepository;
    private final mapper userMapper;


    @Override
    public void registerUser(RegisterRequest request) {

        User newUser = RegisterRequest.toUserTable(request);

        String hashedPassword = hashPassword(request.password());

        newUser.setPassword(hashedPassword);

        var savedUser = userManagementRepository.save(newUser);

        if (!(request.locationDetails() == null)) {
            LocationDetails details = LocationDetailsRequest.toLocationEntity(request.locationDetails());
            details.setUser(savedUser);
            locationRepository.save(details);
        }

    }

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

        return LoginResponse.builder()
                .token(token)
                .userName(user.getId())
                .userName(user.getFirstName())
                .userRole(user.getRole())
                .build();

    }

    @Override
    public UserData findById(String userId) {

        User user = userManagementRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocationDetails location = locationRepository.findByUser(user)
                .orElse(null);

        return userMapper.toUserData(user, location);
    }

    /**
     */
    @Override
    public ValidationResponse validateToken(String authHeader) {
        try {
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return new ValidationResponse(
                        false,
                        null,
                        "Invalid authorization header format"
                );
            }

            String token = authHeader.substring(7);

            if(securityConfig.isTokenExpired(token)) {
                return new ValidationResponse(
                        false,
                        null,
                        "Token has expired"
                );
            }
            String email = securityConfig.extractEmail(token);
            String role = securityConfig.extractRole(token);
            String name = securityConfig.extractClaim(token,
                    claims -> claims.get("name", String.class));

            User user = userManagementRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserInfo userInfo = new UserInfo(
                    user.getId(),
                    user.getEmail(),
                    user.getRole(),
                    user.getFirstName()
            );

            return new ValidationResponse(true, userInfo, "Token is valid");

        } catch (JwtException e) {
            return new ValidationResponse(
                    false,
                    null,
                    "invalid token or invalid token structure"
            );

        }
    }


    private String hashPassword(String password) {

        var argon2 = de.mkammerer.argon2.Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2d);

        return argon2.hash(3, 65536, 1, password);

    }
}
