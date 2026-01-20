package productservice.bookings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import productservice.bookings.dto.*;
import productservice.externalApIs.PesaPalConfigurations;
import productservice.feignclients.authentication.AuthenticationClient;
import productservice.feignclients.authentication.UserData;
import productservice.payment.PaymentConfirmation;
import productservice.payment.PaymentRepository;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.payment.enums.PaymentReason;
import productservice.payment.enums.PaymentStatus;
import productservice.pesapal.PesaPal;
import productservice.room.Room;
import productservice.room.RoomRepository;
import productservice.specifications.BookingSpecification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookingService {

    private final RoomRepository roomRepository;
    private final AuthenticationClient authClient;
    private final BookRoomRepository bookRoomRepository;
    private final AuthenticationClient authenticationClient;
    private final PesaPal pesaPal;
    private final PaymentRepository paymentRepository;
    private final PesaPalConfigurations configurations;

    /**
     *
     */
    @Override
    public BookingResponse bookRoom(BookingRequest request) {

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RuntimeException("room not found for this id"));

        UserData user = authClient.getUserById(request.userId());

        if (user == null) {
            throw new IllegalArgumentException(" user not found with this id");
        }

        if (room.getBookingStatus() == BookingStatus.BOOKED) {
            throw new RuntimeException("Room already booked");
        }

        BookRoom bookRoom = BookRoom.builder()
                .bookingDate(LocalDate.now())
                .room(room)
                .bookingStatus(BookingStatus.UNBOOKED)
                .orderTrackingId("ORDER" + UUID.randomUUID() + "_" + System.currentTimeMillis())
                .userId(request.userId())
                .houseBill(room.getRoomBills().getHouseBill())
                .build();

        BookRoom bookRoom1 = bookRoomRepository.save(bookRoom);
        return new BookingResponse(
                bookRoom1.getOrderTrackingId(),
                bookRoom1.getId(),
                bookRoom1.getUserId(),
                bookRoom1.getRoom().getId(),
                bookRoom1.getHouseBill(),
                bookRoom1.getBookingDate(),
                null
        );

    }

    @Override
    public InitiatePaymentResponse initiateBookingPayment(PaymentRequest request, PaymentReason paymentReason) {

        log.warn("Receiving request {}", request);

        if (paymentReason != PaymentReason.BOOKING) {
            throw new RuntimeException("Invalid Payment reason");
        }

        BookRoom bookRoom = bookRoomRepository.findById(request.bookRoomId())
                .orElseThrow(() -> new RuntimeException("Book request not found for this id:" + request.bookRoomId()));

        String referenceId = UUID.randomUUID().toString();

        PaymentConfirmation payment = PaymentConfirmation.builder()
                .orderTrackingId(bookRoom.getOrderTrackingId())
                .referenceId(referenceId)
                .status(PaymentStatus.INITIATED)
                .paymentTime(LocalDateTime.now())
                .paymentReason(paymentReason)
                .build();
        paymentRepository.save(payment);

        UserData user = authenticationClient.getUserById(request.userId());
        if (user == null) {
            throw new RuntimeException("User not found for this Id: " + request.userId());
        }

        CustomerAddress address = CustomerAddress.builder()
                .phoneNumber(user.phoneNumber())
                .emailAddress(user.emailAddress())
                .countryCode(user.countryCode())
                .firstName(user.firstName())
                .middleName(user.middleName())
                .lastName(user.lastName())
                .line1(user.line1())
                .line2(user.line2())
                .city(user.city())
                .state(user.state())
                .postalCode(user.postalCode())
                .zipCode(user.zipCode())
                .build();

        SubmitRequest submitRequest = SubmitRequest.builder()
                .id(bookRoom.getOrderTrackingId())
                .currency(configurations.getCurrency())
                .amount(BigDecimal.valueOf(request.amount()).setScale(2, RoundingMode.HALF_UP).floatValue())
                .description(request.description())
                .referenceId(referenceId)
                .paymentReason(paymentReason.toString())
                .redirectMode(configurations.getRedirectMode())
                .callbackUrl(configurations.getCallbackUrl())
                .cancellationUrl(configurations.getCancellationUrl())
                .notificationId(configurations.getNotificationId())
                .billingAddress(address)
                .build();

        log.info("Submitting request to PesaPal: {} with amount {}", submitRequest, request.amount());
        return pesaPal.submitOrderRequest(submitRequest);
    }

    /**
     * @param bookRoomId
     * @return
     */
    @Override
    public BookingResponse getBookingRequestById(String bookRoomId) {

        BookRoom bookRoom = bookRoomRepository.findById(bookRoomId)
                .orElseThrow(() -> new RuntimeException("Book entity not found for this id "));

        return BookingResponse.builder()
                .orderTrackingId(bookRoom.getOrderTrackingId())
                .bookRoomId(bookRoom.getRoom().getId())
                .userId(bookRoom.getUserId())
                .amount(bookRoom.getHouseBill())
                .amount(bookRoom.getHouseBill())
                .bookingDate(bookRoom.getBookingDate())
                .bookingStatus(bookRoom.getBookingStatus())
                .build();

    }

    @Override
    public Page<BookingResponse> searchBookings(
            BookingSearchRequest request,
            Pageable pageable
    ) {

        return bookRoomRepository
                .findAll(BookingSpecification.searchBookings(request), pageable)
                .map(bookRoom -> BookingResponse.builder()
                        .bookRoomId(bookRoom.getId())
                        .orderTrackingId(bookRoom.getOrderTrackingId())
                        .userId(bookRoom.getUserId())
                        .roomId(bookRoom.getRoom().getId())
                        .amount(bookRoom.getHouseBill())
                        .bookingDate(bookRoom.getBookingDate())
                        .bookingStatus(bookRoom.getBookingStatus())
                        .build());
    }


}




