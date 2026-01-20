package productservice.bookings.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.math.BigDecimal;

@Builder(toBuilder = true)
public record BookingResponse(

        String orderTrackingId,
        String bookRoomId,
        String userId,
        String roomId,
        String amount,
        LocalDate bookingDate,
        BookingStatus bookingStatus
) {
}
