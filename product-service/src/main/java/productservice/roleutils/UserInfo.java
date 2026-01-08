package productservice.roleutils;

import productservice.payment.dto.DomainRoles;

public record UserInfo(
        String userId,
        String email,
        DomainRoles role
) {
}
