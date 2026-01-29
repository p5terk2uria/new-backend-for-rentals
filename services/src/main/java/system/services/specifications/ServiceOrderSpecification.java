package system.services.specifications;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import system.services.order.ServiceOrder;
import system.services.order.dto.OrderSearchRequest;
import system.services.order.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;


public class ServiceOrderSpecification {

    public static Specification<ServiceOrder> search(OrderSearchRequest request) {

        return ((root, query, criteriaBuilder) -> {


            List<Predicate> predicates = new ArrayList<>();

            if (request.serviceId() != null)
                predicates.add(criteriaBuilder.equal(
                        root.get("serviceId"), request.serviceId()
                ));

            if (request.serviceName() != null && !request.serviceName().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower((root.get("serviceName"))
                        ), "%" + request.serviceName().toLowerCase() + "%"
                ));
            }
            predicates.add(criteriaBuilder.notEqual(root.get("orderStatus"),
                    OrderStatus.PENDING
            ));

            if (request.status() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("orderStatus"), request.status()));
            }
            if (request.fromDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("dateRequested"), request.fromDate()
                ));
            }
            if (request.toDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("dateRequested"), request.toDate()
                ));
            }
            if (request.minBudget() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("budget"), request.minBudget()
                ));
            }
            if (request.maxBudget() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("budget"), request.maxBudget()
                ));
            }
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));


        });
    }
}
