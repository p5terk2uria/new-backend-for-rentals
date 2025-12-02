package productservice.property.services;

import lombok.RequiredArgsConstructor;
import productservice.mapper.PropertyMapper;
import productservice.property.dto.BillsRequest;
import productservice.property.dto.PropertyRequest;
import productservice.property.entities.Property;
import productservice.property.enums.AmenityType;
import productservice.property.repository.AmenitiesRepository;
import productservice.property.repository.BillsRepository;
import productservice.property.repository.PropertyRepository;
import java.util.Set;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class PropertyServiceImpl  implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final AmenitiesRepository amenitiesRepository;
    private final BillsRepository billsRepository;
    private final PropertyMapper propertyMapper;


    @Override
    public void createProperty(PropertyRequest propertyRequest) {
        var property = propertyMapper.toPropertyEntity(propertyRequest);
        var savedProperty =  propertyRepository.save(property);
        createAmenities(propertyRequest.amenities(),savedProperty);
        createPropertyBills(propertyRequest.bills(),savedProperty);
    }


    private void createAmenities(Set<AmenityType> amenityTypes, Property request) {
        var amenities = propertyMapper.toPropertyAmenities(amenityTypes, request);
        amenitiesRepository.saveAll(amenities);
    }


    private void createPropertyBills(Set<BillsRequest> request, Property property) {
        var bills = propertyMapper.toPropertyBills(request, property);
        billsRepository.saveAll(bills);
    }

}
