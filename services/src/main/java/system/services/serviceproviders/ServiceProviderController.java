package system.services.serviceproviders;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.services.config.ApiResponse;
import system.services.config.BaseController;
import system.services.serviceproviders.enums.AvailableStatus;
import system.services.serviceproviders.dto.ServiceProviderRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/services/provider")
public class ServiceProviderController extends BaseController {

    private final ServiceProviderService provideService;

    @PostMapping("/create-provider")
    public ResponseEntity<ApiResponse<?>> createServiceProvider(
            @RequestBody ServiceProviderRequest request
    ) {
        provideService.addServiceProvider(request);
        return ResponseEntity.ok(success("service provider onboarded successfully"));
    }

    @GetMapping("/get-providers-by-service")
    public ResponseEntity<ApiResponse<?>> searchProvidersByService(
            @RequestParam String serviceId,
            Pageable pageable
    ) {
        var response = provideService.getAllServiceProvidersByService(serviceId, pageable);
        return ResponseEntity.ok(success("Services fetched", response));
    }

    @PatchMapping("/update-service-provider-availability")
    public ResponseEntity<ApiResponse<?>> updateServiceProviderAvailability(
            @RequestParam String serviceId,
            @RequestParam AvailableStatus availableStatus
    ) {
        provideService.updateServiceProvideAvailability(serviceId, availableStatus);
        return ResponseEntity.ok(success("update success"));
    }
}
