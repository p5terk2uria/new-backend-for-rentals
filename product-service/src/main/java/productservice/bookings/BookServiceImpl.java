package productservice.bookings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import productservice.bookings.dto.BookingRequest;
import productservice.bookings.dto.BookingResponse;
import productservice.bookings.dto.BookingStatus;
import productservice.bookings.dto.PaymentRequest;
import productservice.feignclients.authentication.AuthenticationClient;
import productservice.feignclients.authentication.UserData;
import productservice.payment.PaymentService;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.payment.enums.PaymentReason;
import productservice.room.Room;
import productservice.room.RoomRepository;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookingService {

    private final RoomRepository roomRepository;
    private final AuthenticationClient authClient;
    private final BookRoomRepository bookRoomRepository;
    private final PaymentService paymentService;

    /**
     * @param request
     */
    @Override
    public BookingResponse bookRoom(BookingRequest request) {

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RuntimeException("room not found for this id"));

        UserData user = authClient.getUserById(request.userId());

        if (user == null) {
            throw new IllegalArgumentException(" user not found with this id");
        }

        BookRoom bookRoom = BookRoom.builder()
                .bookingDate(LocalDate.now())
                .room(room)
                .bookingStatus(BookingStatus.UNBOOKED)
                .orderTrackingId("ORDER" + UUID.randomUUID() + "_" + System.currentTimeMillis())
                .userId(request.userId())
                .houseBill(room.getHouseBill())
                .build();

        BookRoom bookRoom1 = bookRoomRepository.save(bookRoom);
        return new BookingResponse(
                bookRoom1.getOrderTrackingId(),
                bookRoom1.getUserId(),
                bookRoom1.getHouseBill()
        );

    }

    @Override
    public InitiatePaymentResponse initiateBookingPayment(PaymentRequest request) {

        return paymentService.initiatePayment(request, PaymentReason.BOOKING);
    }}


