package productservice.bookings;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import productservice.bookings.dto.*;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.payment.enums.PaymentReason;

public interface BookingService {

    BookingResponse bookRoom(BookingRequest request);

    InitiatePaymentResponse initiateBookingPayment(PaymentRequest request, PaymentReason paymentReason);

    BookingResponse getBookingRequestById (String bookRoomId);

    Page<BookingResponse> searchBookings(BookingSearchRequest request, Pageable pageable);

    void updateBookingStatus(String bookRoomId, BookingStatus desiredStatus);
}
