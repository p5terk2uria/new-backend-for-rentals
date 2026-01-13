package productservice.specifications;


import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import productservice.property.dto.PropertySearchRequest;
import productservice.property.entities.Property;
import productservice.property.entities.PropertyAmenities;
import productservice.property.entities.RoomBills;
import productservice.room.Room;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PropertySpecification {

    public static Specification<Property> searchProperty(PropertySearchRequest request) {

        return new Specification<>() {

            /**
             * @param root
             * @param query
             * @param cb
             * @return
             */
            @Nullable
            @Override
            public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                query.distinct(true);

                Predicate predicate = cb.conjunction();

                Map<String, String> stringFilters = new HashMap<>();

                stringFilters.put("ownerName", request.ownerName());
                stringFilters.put("propertyName", request.propertyName());
                stringFilters.put("propertyLocation", request.propertyLocation());


                for (Map.Entry<String, String> entry : stringFilters.entrySet()) {
                    if (entry.getValue() != null) {
                        predicate = cb.and(predicate,
                                cb.like(cb.lower(root.get(entry.getKey())),
                                        "%" + entry.getValue().toLowerCase() + "%"));
                    }
                }


                if (request.ownerId() != null) {
                    predicate = cb.and(predicate,
                            cb.equal(root.get("ownerId"), request.ownerId()));
                }

                boolean filterRooms = request.houseType() != null
                        || request.minMonthlyBill() != null || request.maxMonthlyBill() != null
                        || request.minMaintenanceBill() != null || request.maxMaintenanceBill() != null
                        || request.minOtherBills() != null || request.maxOtherBills() != null
                        || request.minWaterBill() != null || request.maxWaterBill() != null
                        || request.vacantOnly() != null;

                if (filterRooms) {
                    Subquery<String> roomSubquery = query.subquery(String.class);
                    Root<Room> roomRoot = roomSubquery.from(Room.class);
                    Join<Room, RoomBills> billsJoin = roomRoot.join("roomBills", JoinType.LEFT);

                    roomSubquery.select(roomRoot.get("property").get("id"));

                    Predicate roomPredicate = cb.conjunction();

                    if (request.vacantOnly() != null && request.vacantOnly()) {
                        roomPredicate = cb.and(roomPredicate, cb.isTrue(roomRoot.get("vacant")));
                    }

                    if (request.houseType() != null) {
                        roomPredicate = cb.and(roomPredicate,
                                cb.equal(roomRoot.get("houseType"), request.houseType()));
                    }

                    roomPredicate = addRangeFilter(cb, roomPredicate, billsJoin, "houseBill", request.minMonthlyBill(), request.maxMonthlyBill());
                    roomPredicate = addRangeFilter(cb, roomPredicate, billsJoin, "maintenanceBill", request.minMaintenanceBill(), request.maxMaintenanceBill());
                    roomPredicate = addRangeFilter(cb, roomPredicate, billsJoin, "otherBills", request.minOtherBills(), request.maxOtherBills());
                    roomPredicate = addRangeFilter(cb, roomPredicate, billsJoin, "waterBill", request.minWaterBill(), request.maxWaterBill());

                    roomSubquery.where(cb.and(roomPredicate, cb.equal(roomRoot.get("property"), root)));

                    predicate = cb.and(predicate, root.get("id").in(roomSubquery));
                }

                if (request.amenityType() != null) {
                    Subquery<String> amenitySubquery = query.subquery(String.class);
                    Root<PropertyAmenities> amenityRoot = amenitySubquery.from(PropertyAmenities.class);
                    amenitySubquery.select(amenityRoot.get("property").get("id"));

                    Predicate amenityPredicate = cb.equal(amenityRoot.get("amenityType"), request.amenityType());

                    amenitySubquery.where(cb.and(
                            amenityPredicate,
                            cb.equal(amenityRoot.get("property"), root)
                    ));

                    predicate = cb.and(predicate, root.get("id").in(amenitySubquery));
                }

                return predicate;
            }
        };

    }


    private static Predicate addRangeFilter(CriteriaBuilder cb,
                                            Predicate predicate,
                                            Path<BigDecimal> fieldPath,
                                            BigDecimal min,
                                            BigDecimal max) {

        if (min != null) predicate = cb.and(predicate, cb.greaterThanOrEqualTo(fieldPath, min));
        if (max != null) predicate = cb.and(predicate, cb.lessThanOrEqualTo(fieldPath, max));

        return predicate;
    }

    private static Predicate addRangeFilter(CriteriaBuilder cb,
                                            Predicate predicate,
                                            From<?, ?> join,
                                            String fieldName,
                                            BigDecimal min,
                                            BigDecimal max) {

        return addRangeFilter(cb, predicate, join.get(fieldName), min, max);
    }
}
