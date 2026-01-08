package productservice.bookings.dto;

public record AuthenticationResponse (
        String token,
        String expiryDate,
        Object error,
        String status,
        String message
) {


}
