package productservice.specifications;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import productservice.room.Room;
import productservice.visit.RequestVisit;
import productservice.visit.dto.RequestVisitSearchRequest;

import java.time.LocalDate;

public class RequestVisitSpecification {

    public static Specification<RequestVisit> searchVisits(
            RequestVisitSearchRequest request
    ) {
        return new Specification<>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<RequestVisit> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                query.distinct(true);
                Predicate predicate = criteriaBuilder.conjunction();

                if (request.visitId() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("id"), request.userId()));

                }

                if (request.orderTrackingId() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("userId"), request.userId()));

                }

                if (request.tenantName() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("orderTrackingId"), request.orderTrackingId()));
                }

                if (request.tenantName() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("tenantName"), request.tenantName()));
                }

                if (request.status() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), request.status()));
                }

                Path<LocalDate> visitDatePath = root.get("visitingDate");


                if (request.visitingDateFrom() != null) {
                    predicate = criteriaBuilder.and(predicate,
                            criteriaBuilder.greaterThanOrEqualTo(visitDatePath, request.visitingDateFrom()));
                }

                if (request.visitingDateTo() != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(visitDatePath,
                            request.visitingDateTo()
                    ));
                }

                boolean filterRoom = request.roomId() != null || request.propertyId() != null;

                if (filterRoom) {
                    Join<RequestVisit, Room> roomJoin = root.join("room", JoinType.INNER);

                    if (request.roomId() != null) {
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(roomJoin.get("id"),
                                request.roomId()));
                    }

                    if (request.propertyId() != null) {
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(roomJoin.get("property").get("id"),
                                request.propertyId()
                        ));
                    }
                }

                return predicate;
            }
        };
    }
}


