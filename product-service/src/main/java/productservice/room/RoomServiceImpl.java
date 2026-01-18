package productservice.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import productservice.mapper.RoomMapper;
import productservice.property.entities.Property;
import productservice.property.repository.BillsRepository;
import productservice.property.repository.PropertyRepository;
import productservice.room.dto.RoomRequest;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomMapper roomMapper;
    private final RoomRepository roomRepository;
    private final BillsRepository billsRepository;
    private final PropertyRepository propertyRepository;

    @Override
    public void createRoom(RoomRequest request, String videoLink, Set<String> imageLinks) {

        Property property = propertyRepository.findById(request.propertyId())
                .orElseThrow(() -> new RuntimeException("Property not found with this id"));

        Room room = roomMapper.toRoom(request);
        room.setProperty(property);
        room.setVideoUrl(videoLink);
        room.setImageUrls(imageLinks);
        roomRepository.save(room);

        log.error("Saving  room {}", request);
        log.error ("saving a room {} and room image {}", room.getVideoUrl(), room.getImageUrls());

        billsRepository.save(roomMapper.toRoomBills(request, room));
    }

    @Override
    public void uploadRoomMedia(String roomId, String videoLink, Set<String> imageLinks) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("No room found for this id"));

        room.setImageUrls(imageLinks);
        room.setVideoUrl(videoLink);
        roomRepository.save(room);

    }

    @Override
    public void updateRoomStatus(String roomId, boolean newVacantStatus) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("No room found for this id"));

        boolean currentStatus = room.isVacant();

        if (currentStatus == newVacantStatus) {
            throw new RuntimeException("Room already has this status");
        }
        room.setVacant(newVacantStatus);
        roomRepository.save(room);

    }


}
