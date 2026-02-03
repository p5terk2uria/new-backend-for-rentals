package productservice.management.dto;

import java.time.LocalDate;

public record AssignTenantToRoomRequest(

        String roomId,

        String tenantId,

        LocalDate leaseDate
) {
}
