package productservice.visit.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record VisitRequest(

        String userId,

        LocalDate visitingDate,

        LocalTime visitingTime,

        int noOfVisitors,

        String notes,

        String roomId

) {
}
