package productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.property.dto.PropertyRequest;
import productservice.property.services.PropertyService;

import java.io.IOException;

@RestController
@RequestMapping("api/vi/property")
@Description("Property management API")
@RequiredArgsConstructor
public class Controller extends BaseController {

    private final PropertyService propertyService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createProperty(
            @RequestParam String request,
            @RequestParam MultipartFile file) throws IOException {

        String filePath = propertyService.saveVideo(file);
        PropertyRequest propertyRequest = objectMapper.readValue(request, PropertyRequest.class);

        propertyService.createProperty(propertyRequest, filePath);
        return ResponseEntity.ok(
                success("Property created successfully", null)
        );
    }
}
