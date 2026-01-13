package authentication_service.user.dto;

import authentication_service.user.enums.DomainRoles;
import lombok.Builder;

@Builder(toBuilder = true)
public record LoginResponse (

        String userName,

        String userId,

        String token,

        DomainRoles userRole
){
}
