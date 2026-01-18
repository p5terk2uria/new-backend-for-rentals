package productservice.room.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import productservice.property.enums.HouseType;
import java.util.Set;

@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public record RoomRequest(
        String roomNo,

        String propertyId,

        HouseType houseType,

        @Hidden
        String price,

        Boolean vacant,

        Set<String> imageUrls,

        String videoLink,

        BillsRequest billsRequest
) {
}
