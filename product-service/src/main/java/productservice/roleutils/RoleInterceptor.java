package productservice.roleutils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        RequireRole requireRole = method.getMethodAnnotation(RequireRole.class);

        if (requireRole == null) {
            requireRole = method.getBeanType().getAnnotation(RequireRole.class);
        }

        if (requireRole == null) {
            return true;
        }

        String role = request.getHeader("X-User-Role");

        UserInfo user = (UserInfo) request.getAttribute("currentUser");
        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing user info");
            return false;
        }

        for (String allowed : requireRole.value()) {
            if (allowed.equalsIgnoreCase(role)) {
                return true;
            }
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Insufficient role");
        return false;

    }
}
