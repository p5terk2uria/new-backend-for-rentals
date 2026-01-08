package productservice.feignclients.authentication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "authentication-service", url = "${authentication.service.url}")
public interface AuthenticationClient {

    @GetMapping("api/auth/authentication/feign/user-by-id")
    UserData getUserById(@RequestParam("userId") String userId);
}
