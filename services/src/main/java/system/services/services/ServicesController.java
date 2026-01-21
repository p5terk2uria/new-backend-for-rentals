package system.services.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.services.config.ApiResponse;
import system.services.config.BaseController;
import system.services.services.dto.ServiceRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/service")
public class ServicesController extends BaseController {

    private final PropertyService propertyServices;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createService(
            @RequestBody ServiceRequest request
    ) {
        propertyServices.createService(request);
        return ResponseEntity.ok(success("service created successfully", null));
    }

    @GetMapping("/get-all-services")
    public ResponseEntity<ApiResponse<?>> getAllServices() {

        var response = propertyServices.getAllServices();
        return ResponseEntity.ok(success("success", response));
    }

}
