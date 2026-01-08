package apigateway.utils;

public record UserInfo(
        String userId,
        String email,
        DomainRoles role,
        String name
) {
}
