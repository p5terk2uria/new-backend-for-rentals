package productservice.bookings.dto;

import java.time.LocalDate;

public  record BookingSearchRequest (

        String bookingId,

        String userId,

        String orderTrackingId,

        BookingStatus bookingStatus,

        LocalDate bookingDateFrom,

        LocalDate bookingDateTo,

        String roomId,

        String propertyId
) {
}
