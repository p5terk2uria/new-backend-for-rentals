package productservice.property.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import productservice.config.VideoConfig;
import productservice.feignclients.authentication.AuthenticationClient;
import productservice.feignclients.authentication.UserData;
import productservice.mapper.PropertyMapper;
import productservice.payment.dto.DomainRoles;
import productservice.property.dto.PropertyCreationResponse;
import productservice.property.dto.PropertyRequest;
import productservice.property.dto.PropertyResponse;
import productservice.property.dto.PropertySearchRequest;
import productservice.property.entities.Property;
import productservice.property.entities.PropertyAmenities;
import productservice.property.entities.RoomBills;
import productservice.property.repository.AmenitiesRepository;
import productservice.property.repository.BillsRepository;
import productservice.property.repository.PropertyRepository;
import productservice.room.RoomRepository;
import productservice.room.dto.RoomResponse;
import productservice.specifications.PropertySpecification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final RoomRepository roomRepository;
    private final AmenitiesRepository amenitiesRepository;
    private final PropertyMapper propertyMapper;
    private final VideoConfig videoConfig;
    private final BillsRepository billsRepository;
    private final AuthenticationClient client;

    @Override
    public PropertyCreationResponse createProperty(PropertyRequest request, String videoPath) {

        UserData userData = client.getUserById(request.ownerId());
        if (userData == null) {
            throw new RuntimeException("user not found for this id");
        }
        if (userData.role() != DomainRoles.LAND_LORD) {
            throw new RuntimeException("Owner must be of a LandLord Role");
        }

        Property property = propertyMapper.toEntity(request);
        property.setVideoLink(videoPath);
        Property savedProperty = propertyRepository.save(property);
        Set<PropertyAmenities> amenities = propertyMapper.toPropertyAmenities(request.amenities(), savedProperty);
        amenitiesRepository.saveAll(amenities);
        return PropertyCreationResponse.builder()
                .propertyId(property.getId())
                .propertyName(property.getPropertyName())
                .build();
    }

    @Override
    public Page<PropertyResponse> searchProperty(PropertySearchRequest request, Pageable pageable) {
        var spec = PropertySpecification.searchProperty(request);

        return propertyRepository.findAll(spec, pageable)
                .map(property -> {
                    Set<PropertyAmenities> amenities = amenitiesRepository.findByPropertyId(property.getId());

                    Set<RoomResponse> rooms = roomRepository.findByPropertyId(property.getId()).stream()
                            .map(room -> {

                                RoomBills bills = billsRepository.findRoomBillsByRoomId(room.getId())
                                        .orElse(null);

                                return propertyMapper.toRoomResponse(room, bills);
                            })
                            .collect(Collectors.toSet());

                    return propertyMapper.toResponse(property,
                            amenities.stream().map(PropertyAmenities::getAmenityType).collect(Collectors.toSet()),
                            rooms);
                });
    }

    @Override
    public String savePropertyVideo(MultipartFile file) throws IOException {
        return saveFile(file, videoConfig.getProperty().getVideos());
    }

    @Override
    public String saveRoomVideo(MultipartFile file) throws IOException {
        return saveFile(file, videoConfig.getRoom().getVideos());
    }

    @Override
    public Set<String> saveRoomImages(MultipartFile[] files) throws IOException {
        return saveFiles(files, videoConfig.getRoom().getImages());
    }


    private String saveFile(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) return null;

        Path folderPath = Paths.get(folder).toAbsolutePath();
        Files.createDirectories(folderPath);

        String fileName = generateFileName(file.getOriginalFilename());
        Path targetPath = folderPath.resolve(fileName);

        file.transferTo(targetPath.toFile());

        if (folder.startsWith("/home/dev/files/videos/")) {
            String subFolder = folderPath.getFileName().toString();
            return videoConfig.getBaseUrl() + subFolder + "/" + fileName;
        } else if (folder.startsWith("/home/dev/files/images/")) {
            String subFolder = folderPath.getFileName().toString();
            return "/images/" + subFolder + "/" + fileName;
        }

        return "/" + folderPath.getFileName() + "/" + fileName;
    }


    private Set<String> saveFiles(MultipartFile[] files, String folder) throws IOException {
        if (files == null || files.length == 0) {
            return Set.of();
        }

        return Arrays.stream(files)
                .map(file -> {
                    try {
                        return saveFile(file, folder);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to save file", e);
                    }
                })
                .collect(Collectors.toSet());
    }



    private String generateFileName(String originalFileName) {

        if (originalFileName == null) {
            return UUID.randomUUID().toString();
        }
        return UUID.randomUUID() + "_" + originalFileName.replaceAll("\\s+", "_");
    }

}
