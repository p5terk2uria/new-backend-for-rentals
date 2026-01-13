package productservice.room;

import org.springframework.stereotype.Component;
import productservice.room.dto.RoomRequest;

import java.util.Set;

@Component
public interface RoomService {


    void createRoom(RoomRequest request, String videoLink, Set<String> imageLinks);

    void uploadRoomMedia (String roomId, String videoLink, Set<String> imageLinks);

    void updateRoomStatus (String roomId, boolean isVacant);
}
