package productservice.specifications;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import productservice.bookings.BookRoom;
import productservice.bookings.dto.BookingSearchRequest;
import productservice.room.Room;

public class BookingSpecification {

    public static Specification<BookRoom> searchBookings (BookingSearchRequest request) {

        return  new Specification<BookRoom>() {

            @Nullable
            @Override
            public Predicate toPredicate(Root<BookRoom> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                query.distinct(true);

                Predicate predicate = cb.conjunction();

                if(request.bookingId() != null) {
                    predicate = cb.and(predicate,cb.equal(root.get("id"),request.bookingId()));

                }

                if (request.userId() != null) {
                    predicate = cb.and(predicate, cb.equal(root.get("userId"), request.userId()));
                }

                if(request.orderTrackingId() != null) {
                    predicate = cb.and(predicate, cb.equal(root.get("orderTrackingId"),request.orderTrackingId()));

                }

                if (request.bookingStatus() != null) {
                    predicate = cb.and(
                            predicate,
                            cb.equal(root.get("bookingStatus"), request.bookingStatus())
                    );
                }


                boolean filterRoom = request.roomId() != null || request.propertyId() != null;

                if (filterRoom) {
                    Join<BookRoom, Room> roomJoin =
                            root.join("room", JoinType.INNER);

                    if (request.roomId() != null) {
                        predicate = cb.and(predicate,
                                cb.equal(roomJoin.get("id"), request.roomId()));
                    }

                    if (request.propertyId() != null) {
                        predicate = cb.and(predicate,
                                cb.equal(
                                        roomJoin.get("property").get("id"),
                                        request.propertyId()
                                ));
                    }
                }

                return predicate;
            }
        };
            }



}
