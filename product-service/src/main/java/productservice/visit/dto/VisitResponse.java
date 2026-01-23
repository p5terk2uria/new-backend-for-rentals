package productservice.visit.dto;

import lombok.Builder;
import productservice.visit.RequestVisit;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder(toBuilder = true)
public record VisitResponse(

        String visitId,

        String userId,

        String orderTrackingId,

        String tenantName,

        String phoneNumber,

        String visitingDate,

        String visitingTime,

        int noOfVisitors,

        RequestVisit.RequestStatus visitStatus,

        String roomId,

        String notes
) {
}
