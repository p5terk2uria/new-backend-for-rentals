package productservice.property.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import productservice.property.dto.PropertyRequest;
import productservice.property.dto.PropertyResponse;
import productservice.property.dto.PropertySearchRequest;

import java.io.IOException;

@Component
public interface PropertyService {

    void createProperty(PropertyRequest propertyRequest, String fileName);

    Page<PropertyResponse> searchProperty(PropertySearchRequest request, Pageable pageable);

    String saveVideo(MultipartFile file) throws IOException;
}
