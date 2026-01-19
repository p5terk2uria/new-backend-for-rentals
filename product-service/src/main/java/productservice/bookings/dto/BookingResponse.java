package productservice.bookings.dto;

public record BookingResponse (

        String oderTrackingId,

        String bookRoomId,

        String userId,

        String roomId,

        String amount
) {
}
