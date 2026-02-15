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
import productservice.feignclients.service.*;
import productservice.management.RoomTenant;
import productservice.management.RoomTenantRepository;
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
    private final ServiceClient serviceClient;
    private final RoomTenantRepository roomTenantRepository;

    @Value("${bookings.visit-fee}")
    private Float visitFee;

    @Value("${country.code}")
    private String countryCode;

    @Value("${bookings.service-provider-onboarding-fee}")
    private String serviceProviderFee;

    String referenceId = UUID.randomUUID().toString();

    @Override
    public InitiatePaymentResponse initiatePayment(PaymentRequest request, PaymentReason paymentReason) {

        RequestVisit visit = visitRepository.findById(request.visitId())
                .orElseThrow(() -> new RuntimeException("Request not found for this id: " + request.visitId()));

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
                .countryCode("KE")
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
            case ONBOARDING_COMMISSION -> confirmOnboardingFeePayment(payment);
            case SERVICE_PAYMENT -> confirmServicePayment(payment.getOrderTrackingId());
        }

        return "Payment recorded successfully";
    }

    @Override
    public InitiatePaymentResponse initiateCommissionPayment(PaymentRequest request, PaymentReason reason) {

        log.warn("Receiving payment {}", request);

        if (reason != PaymentReason.ONBOARDING_COMMISSION) {
            throw new RuntimeException("Invalid Payment reason");
        }
        ServiceProviderResponse provider = serviceClient.getProviderById(request.serviceProviderId());

        if (provider == null) {
            throw new RuntimeException("provider not found for this id");
        }
        String referenceId = UUID.randomUUID().toString();
        PaymentConfirmation payment = PaymentConfirmation.builder()
                .orderTrackingId(provider.orderTrackingId())
                .referenceId(referenceId)
                .status(PaymentStatus.INITIATED)
                .paymentTime(LocalDateTime.now())
                .paymentReason(reason)
                .build();
        paymentRepository.save(payment);

        CustomerAddress address = CustomerAddress.builder()
                .phoneNumber(provider.phoneNumber())
                .emailAddress(provider.email())
                .countryCode(countryCode)
                .firstName(provider.name())
                .middleName(provider.name())
                .lastName(provider.name())
                .city(provider.location())
                .state(provider.location())
                .build();

        SubmitRequest submitRequest = SubmitRequest.builder()
                .id(provider.orderTrackingId())
                .currency(configurations.getCurrency())
                .amount(BigDecimal.valueOf(Long.parseLong(serviceProviderFee)).setScale(2, RoundingMode.HALF_UP).floatValue())
                .description("service fee payment")
                .referenceId(referenceId)
                .paymentReason(reason.toString())
                .redirectMode(configurations.getRedirectMode())
                .callbackUrl(configurations.getCallbackUrl())
                .cancellationUrl(configurations.getCancellationUrl())
                .notificationId(configurations.getNotificationId())
                .billingAddress(address)
                .build();
        log.info("Submitting request to PesaPal: {} with amount {}", submitRequest, request.amount());
        return pesaPal.submitOrderRequest(submitRequest);

    }

    @Override
    public InitiatePaymentResponse initiateOrderPayment(PaymentRequest request, PaymentReason reason) {

        if (reason != PaymentReason.SERVICE_PAYMENT)
            throw new RuntimeException("Invalid payment Reason");

        UserData user = authenticationClient.getUserById(request.userId());

        if (user == null) {
            throw new RuntimeException("user not found with this id");
        }

        AdminOrderResponse order = serviceClient.getOrderByOrderTrackingId(request.orderId());

        if (order == null) {
            throw new RuntimeException("Order not found with this id");
        }

        PaymentConfirmation payment = PaymentConfirmation.builder()
                .orderTrackingId(request.orderId())
                .referenceId(referenceId)
                .status(PaymentStatus.INITIATED)
                .paymentTime(LocalDateTime.now())
                .paymentReason(reason)
                .build();
        paymentRepository.save(payment);

        CustomerAddress address = CustomerAddress.builder()
                .phoneNumber(user.phoneNumber())
                .emailAddress(user.emailAddress())
                .countryCode(countryCode)
                .firstName(user.firstName())
                .middleName(user.emailAddress())
                .lastName(user.lastName())
                .city(user.city())
                .state(user.state())
                .build();

        SubmitRequest submitRequest = SubmitRequest.builder()
                .id(order.orderTrackingId())
                .currency(configurations.getCurrency())
                .amount(BigDecimal.valueOf(Long.parseLong(serviceProviderFee)).setScale(2, RoundingMode.HALF_UP).floatValue())
                .description("service fee payment")
                .referenceId(referenceId)
                .paymentReason(reason.toString())
                .redirectMode(configurations.getRedirectMode())
                .callbackUrl(configurations.getCallbackUrl())
                .cancellationUrl(configurations.getCancellationUrl())
                .notificationId(configurations.getNotificationId())
                .billingAddress(address)
                .build();

        log.info("Submitting request to PesaPal: {} with amount {}", submitRequest, request.amount());
        return pesaPal.submitOrderRequest(submitRequest);

    }


    @Override
    public InitiatePaymentResponse initiateRentPayment(PaymentRequest request, PaymentReason reason) {

        if (reason != PaymentReason.RENT_PAYMENT) {
            throw new RuntimeException("Invalid Payment reason");
        }
        RoomTenant roomTenant = roomTenantRepository.findRoomTenantByUserIdAndRoomId(request.userId(), request.roomId())
                .orElseThrow(() -> new RuntimeException("Room with with user not found"));

       PaymentConfirmation payment = PaymentConfirmation.builder()
               .orderTrackingId(roomTenant.getOrderTracking())
               .referenceId(UUID.randomUUID().toString())
               .status(PaymentStatus.INITIATED)
               .paymentTime(LocalDateTime.now())
               .paymentReason(reason)
               .build();
       paymentRepository.save(payment);

        UserData user = authenticationClient.getUserById(request.userId());
        if (user == null) {
            throw new RuntimeException("User not found for this Id: " + request.userId());
        }

        CustomerAddress address = CustomerAddress.builder()
                .phoneNumber(user.phoneNumber())
                .emailAddress(user.emailAddress())
                .countryCode("KE")
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
        Float amount = resolveAmount(reason, request);

        BigDecimal formattedAmount = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);

        SubmitRequest submitRequest = SubmitRequest.builder()
                .id(roomTenant.getOrderTracking())
                .currency(configurations.getCurrency())
                .amount(formattedAmount.floatValue())
                .description(request.description())
                .referenceId(referenceId)
                .paymentReason(reason.toString())
                .redirectMode(configurations.getRedirectMode())
                .callbackUrl(configurations.getCallbackUrl())
                .cancellationUrl(configurations.getCancellationUrl())
                .notificationId(configurations.getNotificationId())
                .billingAddress(address)
                .build();

        log.info("Submitting request to PesaPal: {} with amount {}", submitRequest, formattedAmount);
        return pesaPal.submitOrderRequest(submitRequest);

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
                .orElseThrow(() -> new RuntimeException("booked room not found"));

        room.setBookingStatus(BookingStatus.PENDING);
        roomRepository.save(room);
        bookRoom.setBookingStatus(BookingStatus.PENDING);
        bookRoomRepository.save(bookRoom);
        payment.setBookRoom(bookRoom);
        paymentRepository.save(payment);
    }

    private void confirmOnboardingFeePayment(PaymentConfirmation payment) {

        ServiceProviderResponse provider = serviceClient.getProviderByOrderTrackingId(payment.getOrderTrackingId());

        if (provider == null) {
            throw new RuntimeException("Provider not found for this orderTrackingId");
        }
        serviceClient.updateServiceProviderStatus(provider.id(), AvailableStatus.AVAILABLE);
    }

    private Float resolveAmount(PaymentReason paymentReason, PaymentRequest request) {
        if (paymentReason == PaymentReason.VISIT) {
            return visitFee;
        }
        return request.amount();
    }

    private void confirmServicePayment(String orderTrackingId) {

        AdminOrderResponse order = serviceClient.getOrderByOrderTrackingId(orderTrackingId);

        if (order == null) {
            throw new RuntimeException("Order not found for this orderTrackingId");
        }
        serviceClient.updateOrderPaymentStatus(order.orderTrackingId(), OrderPaymentStatus.PAID);

    }
}
