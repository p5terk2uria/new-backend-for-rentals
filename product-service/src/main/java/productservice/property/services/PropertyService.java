package productservice.property.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import productservice.property.dto.PropertyCreationResponse;
import productservice.property.dto.PropertyRequest;
import productservice.property.dto.PropertyResponse;
import productservice.property.dto.PropertySearchRequest;

import java.io.IOException;
import java.util.Set;

@Component
public interface PropertyService {

    PropertyCreationResponse createProperty(PropertyRequest propertyRequest, String fileName);

    Page<PropertyResponse> searchProperty(PropertySearchRequest request, Pageable pageable);

    String savePropertyVideo(MultipartFile file) throws IOException;

    String saveRoomVideo(MultipartFile file) throws IOException;

    Set<String> saveRoomImages(MultipartFile[] files) throws IOException;

}
