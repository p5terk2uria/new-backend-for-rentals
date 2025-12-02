package productservice.property.services;

import org.springframework.stereotype.Component;
import productservice.property.dto.PropertyRequest;

@Component
public interface PropertyService {

    void createProperty(PropertyRequest propertyRequest);
}
