package productservice.visit;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.reflect.InitializerSignature;
import org.springframework.stereotype.Service;
import productservice.bookings.dto.PaymentRequest;
import productservice.mapper.VisitRequestMapper;
import productservice.payment.PaymentService;
import productservice.payment.dto.InitiatePaymentResponse;
import productservice.payment.enums.PaymentReason;
import productservice.room.Room;
import productservice.room.RoomRepository;
import productservice.visit.dto.VisitRequest;
import productservice.visit.dto.VisitResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestVisitServiceImpl implements RequestVisitService {

    private final RequestVisitRepository visitRepository;
    private final PaymentService paymentService;
    private final RoomRepository roomRepository;
    private final VisitRequestMapper mapper;

    @Override
    public VisitResponse requestVisit(VisitRequest request) {

        productservice.visit.RequestVisit visitEntity = mapper.toVisitEntity(request);

        visitEntity.setStatus(RequestVisit.RequestStatus.NOT_CONFIRMED);

        visitEntity.setOrderTrackingId("ORDER" + UUID.randomUUID() + "-" + System.currentTimeMillis());

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found for this id"));

        visitEntity.setRoom(room);

        visitRepository.save(visitEntity);

        return VisitResponse.builder()
                .visitId(visitEntity.getId())
                .userId(visitEntity.getUserId())
                .build();
    }

    @Override
    public void updateVisitStatus(String userId, String visitId, RequestVisit.RequestStatus currentStatus,
                                  RequestVisit.RequestStatus desiredStatus) {

        RequestVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("no visit entity found for this id %s" + visitId));

        switch (currentStatus) {

            case NOT_CONFIRMED -> {
                if (desiredStatus != RequestVisit.RequestStatus.PENDING) {
                    throw new IllegalArgumentException(
                            "Invalid status change: cannot go from NOT_CONFIRMED to " + desiredStatus
                    );

                }
            }
            case PENDING -> {
                if (desiredStatus != RequestVisit.RequestStatus.VISITED) {
                    throw new IllegalArgumentException(
                            "Invalid status change: cannot go from PENDING to " + desiredStatus

                    );
                }
            }
            case VISITED -> {
                throw new IllegalStateException(
                        "Invalid status change: VISITED is the final status, cannot change further."
                );
            }
            default -> throw new IllegalStateException(
                    "Unknown current status: " + currentStatus
            );
        }
        visit.setStatus(desiredStatus);
        visitRepository.save(visit);
    }

    /**
     * @param request
     * @param paymentReason
     * @return
     */
    @Override
    public InitiatePaymentResponse initiatePayment(PaymentRequest request, PaymentReason paymentReason) {

        return paymentService.initiatePayment(request, PaymentReason.VISIT);

    }


}
