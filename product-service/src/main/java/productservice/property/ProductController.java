package productservice.property;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.property.dto.PropertyRequest;
import productservice.property.dto.PropertyResponse;
import productservice.property.dto.PropertySearchRequest;
import productservice.property.enums.AmenityType;
import productservice.property.enums.HouseType;
import productservice.room.RoomService;
import productservice.room.dto.RoomRequest;
import productservice.property.services.PropertyService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/api/products/property")
@Description("Property management API")
@RequiredArgsConstructor
@Slf4j
public class ProductController extends BaseController {

    private final PropertyService propertyService;
    private final ObjectMapper objectMapper;
    private final RoomService roomService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createProperty(
            @RequestParam String request,
            @RequestParam MultipartFile file) throws IOException {

        String filePath = propertyService.savePropertyVideo(file);
        PropertyRequest propertyRequest = objectMapper.readValue(request, PropertyRequest.class);

        var response = propertyService.createProperty(propertyRequest, filePath);
        return ResponseEntity.ok(
                success("Property created successfully", response)
        );
    }

    @GetMapping("/search")
    public ApiResponse<Page<PropertyResponse>> searchProperty(
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) String propertyId,
            @RequestParam(required = false) String propertyName,
            @RequestParam(required = false) String propertyLocation,
            @RequestParam(required = false) HouseType houseType,
            @RequestParam(required = false) AmenityType amenityType,
            @RequestParam(required = false) BigDecimal minMonthlyBill,
            @RequestParam(required = false) BigDecimal maxMonthlyBill,
            @RequestParam(required = false) BigDecimal minWaterBill,
            @RequestParam(required = false) BigDecimal maxWaterBill,
            @RequestParam(required = false) BigDecimal minTrashBill,
            @RequestParam(required = false) BigDecimal maxTrashBill,
            @RequestParam(required = false) BigDecimal minMaintenanceBill,
            @RequestParam(required = false) BigDecimal maxMaintenanceBill,
            @RequestParam(required = false) BigDecimal minOtherBills,
            @RequestParam(required = false) BigDecimal maxOtherBills,
            @RequestParam(required = false) Boolean vacantOnly,
            Pageable pageable
    ) {

        var request = new PropertySearchRequest(
                ownerId, ownerName,propertyId, propertyName,
                propertyLocation, houseType,
                amenityType, minMonthlyBill,
                maxMonthlyBill, minWaterBill,
                maxWaterBill, minTrashBill,
                maxTrashBill, minMaintenanceBill,
                maxMaintenanceBill, minOtherBills,
                maxOtherBills, vacantOnly
        );
        Page<PropertyResponse> responsePage = propertyService.searchProperty(request, pageable);
        return new ApiResponse<>(true, "Fetched properties", responsePage);
    }

    @PostMapping(value = "/create-room", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createRoom(
            @RequestParam String request,
            @RequestPart(required = false) MultipartFile roomVideo,
            @RequestPart(required = false) MultipartFile[] roomImages
    ) throws IOException {

        RoomRequest roomRequest = objectMapper.readValue(request, RoomRequest.class);

        String videoLink = propertyService.saveRoomVideo(roomVideo);
        Set<String> imageLinks = propertyService.saveRoomImages(roomImages);

        roomService.createRoom(roomRequest, videoLink, imageLinks);
        return ResponseEntity.ok(success("Room created successfully"));
    }

    @PostMapping(value = "/upload-room-media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> uploadVideoImage(
            @RequestParam String roomId,
            @RequestParam(required = false) MultipartFile roomVideo,
            @RequestParam(required = false) MultipartFile[] roomImages
    ) throws IOException {
        String videoLink = propertyService.saveRoomVideo(roomVideo);
        Set<String> imageLinks = propertyService.saveRoomImages(roomImages);
        roomService.uploadRoomMedia(roomId, videoLink, imageLinks);
        return ResponseEntity.ok(success("success upload"));

    }

    @PostMapping(value = "/update-room-status")
    public ResponseEntity<ApiResponse<?>> updateRoomStatus(
            @RequestParam boolean newStatus,
            @RequestParam String roomId
    ) {
        roomService.updateRoomStatus(roomId, newStatus);
        return ResponseEntity.ok(success("success"));
    }

}
