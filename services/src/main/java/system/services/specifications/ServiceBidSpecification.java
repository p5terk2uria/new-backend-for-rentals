package system.services.specifications;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import system.services.bidorder.ServiceBidSearchRequest;
import system.services.bidorder.ServiceOrderBid;

import java.util.ArrayList;
import java.util.List;

public class ServiceBidSpecification {

    public static Specification<ServiceOrderBid> search(ServiceBidSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.serviceProviderId() != null && !request.serviceProviderId().isBlank()) {
                predicates.add(cb.equal(root.get("serviceProviderId"), request.serviceProviderId()));
            }

            if (request.orderId() != null && !request.orderId().isBlank()) {
                predicates.add(cb.equal(root.get("orderId"), request.orderId()));
            }

            if (request.status() != null) {
                predicates.add(cb.equal(root.get("status"), request.status()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
