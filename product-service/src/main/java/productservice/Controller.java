package productservice;

import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.property.dto.PropertyRequest;
import productservice.property.services.PropertyService;

@RestController
@RequestMapping("api/vi/property")
@Description("Property management API")
@RequiredArgsConstructor
public class Controller extends BaseController {

    private final PropertyService propertyService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createProperty(
            @RequestBody PropertyRequest propertyRequest) {

        propertyService.createProperty(propertyRequest);
        return ResponseEntity.ok(
                success("Property created successfully", null)
        );
    }
}
