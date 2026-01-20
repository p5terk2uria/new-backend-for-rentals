package productservice.feignclients.authentication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import productservice.payment.dto.DomainRoles;

import java.util.List;

@FeignClient(name = "authentication-service", url = "${authentication.service.url}")
public interface AuthenticationClient {

    @GetMapping("/authentication/feign/user-by-id")
    UserData getUserById(@RequestParam("userId") String userId);

    @GetMapping("/authentication/get-users-by-role")
    List<UserData> getUsersByDomainRole(@RequestParam("role") DomainRoles role);
}
