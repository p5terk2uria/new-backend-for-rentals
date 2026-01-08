package productservice.visits;

import org.springframework.stereotype.Service;
import productservice.visits.dto.VisitRequest;

@Service
public interface RequestVisitService {

    void requestVisit(VisitRequest request);

    void updateVisitStatus(String userId,String visitId, RequestVisit.RequestStatus currentStatus,
                           RequestVisit.RequestStatus desiredStatus);
}
