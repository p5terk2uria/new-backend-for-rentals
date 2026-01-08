package apigateway.utils;

public record ValidationResponse(
        boolean valid,
        UserInfo userInfo,
        String message
){

}
