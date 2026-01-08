package authentication_service.user.dto;

public record ValidationResponse (
        boolean valid,
        UserInfo userInfo,
        String message
){

}
