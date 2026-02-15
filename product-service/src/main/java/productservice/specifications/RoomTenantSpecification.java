package productservice.specifications;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import productservice.management.RoomTenant;
import productservice.management.dto.TenantFilterRequest;

import java.util.HashMap;
import java.util.Map;

public class RoomTenantSpecification {

    public static Specification<RoomTenant> searchRoomTenant(TenantFilterRequest request) {

        return new Specification<>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<RoomTenant> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                query.distinct(true);

                Predicate predicate = cb.conjunction();

                Map<String, String> stringFilters = new HashMap<>();
                stringFilters.put("userName", request.userName());
                stringFilters.put("email", request.email());

                for (Map.Entry<String, String> entry : stringFilters.entrySet()) {
                    if (entry.getValue() != null) {
                        predicate = cb.and(predicate,
                                cb.like(cb.lower(root.get(entry.getKey())),
                                        "%" + entry.getValue().toLowerCase() + "%"));
                    }
                }

                if (request.propertyId() != null) {
                    predicate = cb.and(predicate,
                            cb.equal(root.get("propertyId"), request.propertyId()));
                }

                if (request.roomId() != null) {
                    predicate = cb.and(predicate,
                            cb.equal(root.get("roomId"), request.roomId()));
                }

                if (request.activeOnly() != null && request.activeOnly()) {
                    predicate = cb.and(predicate,
                            cb.isTrue(root.get("active")));
                }

                if (request.paymentStatus() != null) {
                    predicate = cb.and(predicate,
                            cb.equal(root.get("paymentStatus"), request.paymentStatus()));
                }

                return predicate;
            }
        };
    }
}