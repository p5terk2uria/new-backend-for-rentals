package productservice.specifications;

import jakarta.persistence.criteria.*;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import productservice.property.dto.PropertySearchRequest;
import productservice.property.entities.Property;
import productservice.property.entities.PropertyBills;
import productservice.property.entities.PropertyAmenities;

import java.math.BigDecimal;
import java.util.Map;

public class PropertySpecification {

    public static Specification<Property> searchProperty(PropertySearchRequest request) {

        return new Specification<Property>() {
            @Override
            public Predicate toPredicate(
                    @NonNull Root<Property> root,
                    CriteriaQuery<?> query,
                    @NonNull CriteriaBuilder criteriaBuilder
            ) {

                query.distinct(true);

                Predicate predicate = criteriaBuilder.conjunction();

                Map<String, String> stringFilters = new java.util.HashMap<>();
                if (request.ownerName() != null) stringFilters.put("ownerName", request.ownerName());
                if (request.propertyName() != null) stringFilters.put("propertyName", request.propertyName());
                if (request.propertyLocation() != null) stringFilters.put("propertyLocation", request.propertyLocation());

                for (Map.Entry<String, String> entry : stringFilters.entrySet()) {
                    String field = entry.getKey();
                    String value = entry.getValue();
                    if (value != null) {
                        predicate = criteriaBuilder.and(
                                predicate,
                                criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get(field)),
                                        "%" + value.toLowerCase() + "%"
                                )
                        );
                    }
                }

                if (request.ownerId() != null) {
                    predicate = criteriaBuilder.and(
                            predicate,
                            criteriaBuilder.equal(root.get("ownerId"), request.ownerId())
                    );
                }

                if (request.houseType() != null) {
                    predicate = criteriaBuilder.and(
                            predicate,
                            criteriaBuilder.equal(root.get("houseType"), request.houseType())
                    );
                }

                boolean filterBills = request.minMonthlyBill() != null || request.maxMonthlyBill() != null
                        || request.minMaintenanceBill() != null || request.maxMaintenanceBill() != null
                        || request.minOtherBills() != null || request.maxOtherBills() != null
                        || request.minWaterBill() != null || request.maxWaterBill() != null;

                if (filterBills) {
                    Subquery<String> subquery = query.subquery(String.class);
                    Root<PropertyBills> billRoot = subquery.from(PropertyBills.class);
                    subquery.select(billRoot.get("property").get("id"));

                    Predicate billPredicate = criteriaBuilder.conjunction();
                    billPredicate = addRangeFilter(criteriaBuilder, billPredicate, billRoot, "houseBill", request.minMonthlyBill(), request.maxMonthlyBill());
                    billPredicate = addRangeFilter(criteriaBuilder, billPredicate, billRoot, "maintenanceBill", request.minMaintenanceBill(), request.maxMaintenanceBill());
                    billPredicate = addRangeFilter(criteriaBuilder, billPredicate, billRoot, "otherBills", request.minOtherBills(), request.maxOtherBills());
                    billPredicate = addRangeFilter(criteriaBuilder, billPredicate, billRoot, "waterBill", request.minWaterBill(), request.maxWaterBill());

                    subquery.where(criteriaBuilder.and(
                            billPredicate,
                            criteriaBuilder.equal(billRoot.get("property"), root)
                    ));

                    predicate = criteriaBuilder.and(predicate, root.get("id").in(subquery));
                }

                if (request.amenityType() != null) {
                    Subquery<String> amenitySubquery = query.subquery(String.class);
                    Root<PropertyAmenities> amenityRoot = amenitySubquery.from(PropertyAmenities.class);
                    amenitySubquery.select(amenityRoot.get("property").get("id"));

                    Predicate amenityPredicate = criteriaBuilder.equal(amenityRoot.get("amenityType"), request.amenityType());

                    amenitySubquery.where(criteriaBuilder.and(
                            amenityPredicate,
                            criteriaBuilder.equal(amenityRoot.get("property"), root)
                    ));

                    predicate = criteriaBuilder.and(predicate, root.get("id").in(amenitySubquery));
                }

                return predicate;
            }
        };
    }

    private static Predicate addRangeFilter(CriteriaBuilder criteriaBuilder,
                                            Predicate predicate,
                                            Path<BigDecimal> fieldPath,
                                            BigDecimal min,
                                            BigDecimal max) {

        if (min != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(fieldPath, min));
        }
        if (max != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(fieldPath, max));
        }
        return predicate;
    }

    private static Predicate addRangeFilter(CriteriaBuilder criteriaBuilder,
                                            Predicate predicate,
                                            From<?, ?> join,
                                            String fieldName,
                                            BigDecimal min,
                                            BigDecimal max) {
        return addRangeFilter(criteriaBuilder, predicate, join.get(fieldName), min, max);
    }
}
