package productservice.visit;

import org.springframework.stereotype.Service;
import productservice.bookings.dto.PaymentRequest;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.payment.enums.PaymentReason;
import productservice.visit.dto.VisitRequest;
import productservice.visit.dto.VisitResponse;

@Service
public interface RequestVisitService {

    VisitResponse requestVisit(VisitRequest request);

    void updateVisitStatus(String userId,String visitId, RequestVisit.RequestStatus currentStatus,
                           RequestVisit.RequestStatus desiredStatus);

    InitiatePaymentResponse initiatePayment (PaymentRequest request, PaymentReason paymentReason);

    VisitResponse getVisitById (String visitId);
}
