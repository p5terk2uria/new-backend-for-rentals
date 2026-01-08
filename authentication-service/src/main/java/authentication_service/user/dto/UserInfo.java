package authentication_service.user.dto;

import authentication_service.user.enums.DomainRoles;

public record UserInfo (
        String userId,
        String email,
        DomainRoles role,
        String name
) {
}
