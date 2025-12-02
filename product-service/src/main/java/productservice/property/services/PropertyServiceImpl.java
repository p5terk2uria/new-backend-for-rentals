package productservice.property.services;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import productservice.config.VideoConfig;
import productservice.mapper.PropertyMapper;
import productservice.property.dto.BillsRequest;
import productservice.property.dto.PropertyRequest;
import productservice.property.entities.Property;
import productservice.property.enums.AmenityType;
import productservice.property.repository.AmenitiesRepository;
import productservice.property.repository.BillsRepository;
import productservice.property.repository.PropertyRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final AmenitiesRepository amenitiesRepository;
    private final BillsRepository billsRepository;
    private final PropertyMapper propertyMapper;
    private final VideoConfig videoConfig;


    @Override
    public void createProperty(PropertyRequest propertyRequest, String filePath) {
        var property = propertyMapper.toPropertyEntity(propertyRequest);
        property.setVideoLink(filePath);
        var savedProperty = propertyRepository.save(property);
        createAmenities(propertyRequest.amenities(), savedProperty);
        createPropertyBills(propertyRequest.bills(), savedProperty);
    }


    private void createAmenities(Set<AmenityType> amenityTypes, Property request) {
        var amenities = propertyMapper.toPropertyAmenities(amenityTypes, request);
        amenitiesRepository.saveAll(amenities);
    }


    private void createPropertyBills(Set<BillsRequest> request, Property property) {
        var bills = propertyMapper.toPropertyBills(request, property);
        billsRepository.saveAll(bills);
    }


    @Override
    public String saveVideo(MultipartFile file) throws IOException {

        Path folderPath = Paths.get(videoConfig.getFolder());

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }
        String originalFileName = file.getOriginalFilename();

        assert originalFileName != null;
        String fileName = UUID.randomUUID() + "_" + originalFileName.replaceAll("\\s+", "_");

        Path filePath = Paths.get(videoConfig.getFolder(), fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return videoConfig.getBaseUrl() + fileName;

    }

}
