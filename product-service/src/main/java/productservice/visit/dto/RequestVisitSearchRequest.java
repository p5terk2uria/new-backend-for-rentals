package productservice.visit.dto;

import productservice.visit.RequestVisit;

import java.time.LocalDate;

public record RequestVisitSearchRequest (

        String visitId,

        String userId,

        String orderTrackingId,

        String tenantName,

        RequestVisit.RequestStatus status,

        LocalDate visitingDateFrom,

        LocalDate visitingDateTo,

        String roomId,

        String propertyId
) {
}
