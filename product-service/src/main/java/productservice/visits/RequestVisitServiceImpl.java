package productservice.visits;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import productservice.mapper.VisitRequestMapper;
import productservice.visits.dto.VisitRequest;

@Service
@RequiredArgsConstructor
public class RequestVisitServiceImpl implements RequestVisitService {

    private final RequestVisitRepository visitRepository;
    private final VisitRequestMapper mapper;

    @Override
    public void requestVisit(VisitRequest request) {

        productservice.visits.RequestVisit visitEntity = mapper.toVisitEntity(request);

        visitEntity.setStatus(RequestVisit.RequestStatus.NOT_CONFIRMED);

        visitEntity.setOrderTrackingId("ORDER" + request.userId() + "-" + System.currentTimeMillis());

        visitRepository.save(visitEntity);
    }


    @Override
    public void updateVisitStatus(String userId,String visitId, RequestVisit.RequestStatus currentStatus,
                                  RequestVisit.RequestStatus desiredStatus) {

        RequestVisit visit = visitRepository.findById(visitId)
                .orElseThrow(()->new IllegalArgumentException("no visit entity found for this id %s" + visitId));

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


}
