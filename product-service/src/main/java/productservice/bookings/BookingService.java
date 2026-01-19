package productservice.bookings;

import productservice.bookings.dto.BookingRequest;
import productservice.bookings.dto.BookingResponse;
import productservice.bookings.dto.PaymentRequest;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.payment.enums.PaymentReason;

public interface BookingService {

    BookingResponse bookRoom(BookingRequest request);

    InitiatePaymentResponse initiateBookingPayment(PaymentRequest request, PaymentReason paymentReason);
}
