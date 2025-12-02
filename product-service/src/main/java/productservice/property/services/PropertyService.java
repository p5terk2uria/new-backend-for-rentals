package productservice.property.services;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import productservice.property.dto.PropertyRequest;

import java.io.IOException;

@Component
public interface PropertyService {

    void createProperty(PropertyRequest propertyRequest, String fileName);

    String saveVideo(MultipartFile file) throws IOException;
}
