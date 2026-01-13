package productservice.mapper;

import org.springframework.stereotype.Component;
import productservice.property.entities.RoomBills;
import productservice.room.dto.RoomRequest;
import productservice.room.Room;


@Component
public class RoomMapper {

    public Room toRoom(RoomRequest request) {

        return  Room.builder()
                .houseBill(request.price())
                .imageUrls(request.imageUrls())
                .videoUrl(request.videoLink())
                .build();
    }

    public RoomBills toRoomBills(RoomRequest request, Room room) {

        return RoomBills.builder()
                .room(room)
                .houseBill(request.billsRequest().houseBill())
                .trashBill(request.billsRequest().trashBill())
                .waterBill(request.billsRequest().waterBill())
                .maintenanceBill(request.billsRequest().maintenanceBill())
                .otherBills(request.billsRequest().otherBills())
                .build();

    }}



