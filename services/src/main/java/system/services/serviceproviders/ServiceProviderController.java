package system.services.serviceproviders;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import system.services.config.ApiResponse;
import system.services.config.BaseController;
import system.services.serviceproviders.enums.ServiceProviderRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/services/provider")
public class ServiceProviderController extends BaseController {

    private  final ServiceProviderService provideService;

    @PostMapping("/create-provider")
    public ResponseEntity<ApiResponse<?>> createServiceProvider (
            @RequestBody ServiceProviderRequest request
            ){
        provideService.addServiceProvider(request);
        return ResponseEntity.ok(success("service provider onboarded successfully"));
    }
}
