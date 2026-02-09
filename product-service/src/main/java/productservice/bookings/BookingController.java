package productservice.bookings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import productservice.bookings.dto.BookingRequest;
import productservice.bookings.dto.BookingSearchRequest;
import productservice.bookings.dto.BookingStatus;
import productservice.bookings.dto.PaymentRequest;
import productservice.config.ApiResponse;
import productservice.config.BaseController;
import productservice.payment.enums.PaymentReason;

import java.time.LocalDate;

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

        BookingRequest request = new BookingRequest(userId, roomId);

        return ResponseEntity.ok(
                success("success", bookingService.bookRoom(request)));
    }

    @PostMapping("/book-pay")
    public ResponseEntity<ApiResponse<?>> payBookingBill(
            @RequestBody PaymentRequest request
    ) {
        return ResponseEntity.ok(success("success",
                bookingService.initiateBookingPayment(request, PaymentReason.BOOKING)));
    }

    @GetMapping("/get-booking-by-id")
    public ResponseEntity<ApiResponse<?>> getBookingById(
            @RequestParam String bookingId
    ) {
        return ResponseEntity.ok(
                success("success", bookingService.getBookingRequestById(bookingId)));
    }

    @GetMapping("/search")
    public ApiResponse<Page<?>> searchBookings(
            @RequestParam(required = false) String bookingId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String orderTrackingId,
            @RequestParam(required = false) BookingStatus bookingStatus,
            @RequestParam(required = false) LocalDate bookingDateFrom,
            @RequestParam(required = false) LocalDate bookingDateTo,
            @RequestParam(required = false) String roomId,
            @RequestParam(required = false) String propertyId,
            Pageable pageable
    ) {

        var request = new BookingSearchRequest(
                bookingId,
                userId,
                orderTrackingId,
                bookingStatus,
                bookingDateFrom,
                bookingDateTo,
                roomId,
                propertyId
        );

        Page<?> responsePage = bookingService.searchBookings(request, pageable);
        return new ApiResponse<>(true, "Fetched bookings", responsePage);
    }

    @PostMapping("/update-booking-status")
    public ResponseEntity<ApiResponse<?>> updateBookingStatus(String bookRoomId, BookingStatus desiredStatus){
        bookingService.updateBookingStatus(bookRoomId,desiredStatus);
        return ResponseEntity.ok(success(null));
    }



}
