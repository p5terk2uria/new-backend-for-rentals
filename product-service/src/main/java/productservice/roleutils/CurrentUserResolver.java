package productservice.roleutils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import productservice.payment.dto.DomainRoles;

@Component
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

    @Value("${gateway.secret}")
    private String gatewaySecret;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null
                && parameter.getParameterType().equals(UserInfo.class);

    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        String secret = request.getHeader("X-Gateway-Secret");
        if (!gatewaySecret.equals(secret)) {
            throw new RuntimeException("Unauthorised Access");
        }

        String userId = request.getHeader("X-User-Id");
        String email = request.getHeader("X-User-Email");
        String role = request.getHeader("X-User-Role");

        if (userId == null || role == null) {
            throw new RuntimeException("Unauthorized: Missing user headers");
        }

        UserInfo user = new UserInfo(userId, email, DomainRoles.valueOf(role));

        request.setAttribute("currentUser", user);

        return user;

    }
}
