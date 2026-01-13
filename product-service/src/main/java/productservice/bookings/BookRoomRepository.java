package productservice.bookings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRoomRepository extends JpaRepository <BookRoom, String> {

    Optional<BookRoom> findByOrderTrackingId(String orderTrackingId);
}
