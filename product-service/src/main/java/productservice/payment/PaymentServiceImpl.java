package productservice.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import productservice.bookings.BookRoom;
import productservice.bookings.BookRoomRepository;
import productservice.bookings.dto.BookingStatus;
import productservice.bookings.dto.CustomerAddress;
import productservice.bookings.dto.PaymentRequest;
import productservice.bookings.dto.SubmitRequest;
import productservice.externalApIs.PesaPalConfigurations;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.pesapal.PesaPal;
import productservice.feignclients.authentication.AuthenticationClient;
import productservice.feignclients.authentication.UserData;
import productservice.payment.dto.CallBackResponse;
import productservice.payment.enums.PaymentReason;
import productservice.payment.enums.PaymentStatus;
import productservice.room.Room;
import productservice.room.RoomRepository;
import productservice.visit.RequestVisit;
import productservice.visit.RequestVisitRepository;

import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RoomRepository roomRepository;
    private final RequestVisitRepository visitRepository;
    private final BookRoomRepository bookRoomRepository;
    private final PesaPal pesaPal;
    private final PesaPalConfigurations configurations;
    private final AuthenticationClient authenticationClient;

    @Value("${bookings.visit-fee}")
    private Float visitFee;

    @Override
    public InitiatePaymentResponse initiatePayment(PaymentRequest request, PaymentReason paymentReason) {

        RequestVisit visit = visitRepository.findById(request.visitId())
                .orElseThrow(() -> new RuntimeException("Request not found for this id: " + request.visitId()));

        String referenceId = UUID.randomUUID().toString();

        PaymentConfirmation payment = PaymentConfirmation.builder()
                .orderTrackingId(visit.getOrderTrackingId())
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

        Float amount = resolveAmount(paymentReason, request);

        BigDecimal formattedAmount = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);

        SubmitRequest submitRequest = SubmitRequest.builder()
                .id(visit.getOrderTrackingId())
                .currency(configurations.getCurrency())
                .amount(formattedAmount.floatValue())
                .description(request.description())
                .referenceId(referenceId)
                .paymentReason(paymentReason.toString())
                .redirectMode(configurations.getRedirectMode())
                .callbackUrl(configurations.getCallbackUrl())
                .cancellationUrl(configurations.getCancellationUrl())
                .notificationId(configurations.getNotificationId())
                .billingAddress(address)
                .build();

        log.info("Submitting request to PesaPal: {} with amount {}", submitRequest, formattedAmount);
        return pesaPal.submitOrderRequest(submitRequest);
    }

    @Override
    public String recordRequestVisitCallBack(CallBackResponse response) {

        PaymentConfirmation payment = paymentRepository
                .findByOrderTrackingId(response.merchantReferenceId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return "Payment already processed";
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setMerchantReference(response.merchantReferenceId());
        payment.setPaymentTime(LocalDateTime.now());
        paymentRepository.save(payment);

        switch (payment.getPaymentReason()) {
            case VISIT -> confirmVisitPayment(payment);
            case BOOKING -> confirmBookingPayment(payment);
        }

        return "Payment recorded successfully";
    }

    private void confirmVisitPayment(PaymentConfirmation payment) {
        RequestVisit visit = visitRepository.findByOrderTrackingId(payment.getMerchantReference())
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        visit.setStatus(RequestVisit.RequestStatus.PENDING);
        visitRepository.save(visit);
        payment.setRequestVisit(visit);
        paymentRepository.save(payment);
    }

    private void confirmBookingPayment(PaymentConfirmation payment) {
       BookRoom bookRoom = bookRoomRepository.findByOrderTrackingId(payment.getOrderTrackingId())
               .orElseThrow(() -> new RuntimeException("room not found for this id"));

       Room room = roomRepository.findById(bookRoom.getRoom().getId())
                       .orElseThrow(()-> new RuntimeException("booked room not found"));

       room.setBookingStatus(BookingStatus.BOOKED);
       roomRepository.save(room);
       bookRoom.setBookingStatus(BookingStatus.BOOKED);
       bookRoomRepository.save(bookRoom);
       payment.setBookRoom(bookRoom);
       paymentRepository.save(payment);
    }

    private Float resolveAmount(PaymentReason paymentReason, PaymentRequest request) {
        if (paymentReason == PaymentReason.VISIT) {
            return visitFee;
        }
        return request.amount();
    }
}
