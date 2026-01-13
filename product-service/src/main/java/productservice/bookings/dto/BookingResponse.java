package productservice.bookings.dto;

public record BookingResponse (

        String oderTrackingId,

        String userId,

        String amount
) {
}
