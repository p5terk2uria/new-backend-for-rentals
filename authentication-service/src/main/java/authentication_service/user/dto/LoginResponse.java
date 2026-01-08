package authentication_service.user.dto;

import authentication_service.user.enums.DomainRoles;

public record LoginResponse (

        String userName,

        String userId,

        String token,

        DomainRoles userRole
){
}
