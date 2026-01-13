package productservice.property.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import productservice.config.VideoConfig;
import productservice.mapper.PropertyMapper;
import productservice.property.dto.PropertyRequest;
import productservice.property.dto.PropertyResponse;
import productservice.property.dto.PropertySearchRequest;
import productservice.property.entities.Property;
import productservice.property.entities.PropertyAmenities;
import productservice.property.repository.AmenitiesRepository;
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
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final RoomRepository roomRepository;
    private final AmenitiesRepository amenitiesRepository;
    private final PropertyMapper propertyMapper;
    private final VideoConfig videoConfig;

    @Override
    public void createProperty(PropertyRequest request, String videoPath) {
        Property property = propertyMapper.toEntity(request);
        property.setVideoLink(videoPath);
        Property savedProperty = propertyRepository.save(property);
        Set<PropertyAmenities> amenities = propertyMapper.toPropertyAmenities(request.amenities(), savedProperty);
        amenitiesRepository.saveAll(amenities);
    }

    @Override
    public Page<PropertyResponse> searchProperty(PropertySearchRequest request, Pageable pageable) {
        var spec = PropertySpecification.searchProperty(request);

        return propertyRepository.findAll(spec, pageable)
                .map(property -> {
                    Set<PropertyAmenities> amenities = amenitiesRepository.findByPropertyId(property.getId());

                    Set<RoomResponse> rooms =
                            roomRepository.findByPropertyId(property.getId()).stream()
                                    .filter(room ->
                                            request.vacantOnly() == null ||
                                                    !request.vacantOnly() ||
                                                    room.isVacant()
                                    )
                                    .map(propertyMapper::toRoomResponse)
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

        if (file == null || file.isEmpty()) {
            return null;
        }

        Path folderPath = Paths.get(folder);
        Files.createDirectories(folderPath);

        String fileName = generateFileName(file.getOriginalFilename());
        return videoConfig.getBaseUrl() + folderPath.resolve(fileName);
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
