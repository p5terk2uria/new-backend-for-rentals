package productservice.bookings;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import productservice.bookings.dto.BookingRequest;
import productservice.bookings.dto.PaymentRequest;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.payment.enums.PaymentReason;

@Slf4j
@Repository
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/bookings")
public class BookingController extends BaseController {

    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<?>> bookRoom(
            @RequestParam String userId,
            @RequestParam String roomId
            ) {

        BookingRequest request = new BookingRequest(userId,roomId);

        return ResponseEntity.ok(success(bookingService.bookRoom(request)));
    }

    @PostMapping("/book-pay")
    public  ResponseEntity<ApiResponse<?>> payBookingBill (
            @RequestBody PaymentRequest request
            ) {
        return ResponseEntity.ok(success(bookingService.initiateBookingPayment(request, PaymentReason.BOOKING)));
    }


}
