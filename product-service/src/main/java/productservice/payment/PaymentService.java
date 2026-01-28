package productservice.payment;

import org.springframework.stereotype.Service;
import productservice.bookings.dto.PaymentRequest;
import productservice.payment.dto.CallBackResponse;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.payment.enums.PaymentReason;

@Service
public interface PaymentService {

    InitiatePaymentResponse initiatePayment (PaymentRequest request, PaymentReason paymentReason);

    String recordRequestVisitCallBack(CallBackResponse response);

    InitiatePaymentResponse initiateCommissionPayment (PaymentRequest request, PaymentReason reason);

}
