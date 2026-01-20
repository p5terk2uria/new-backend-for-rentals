package productservice.bookings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRoomRepository extends JpaRepository<BookRoom, String>,
        JpaSpecificationExecutor<BookRoom> {

    Optional<BookRoom> findByOrderTrackingId(String orderTrackingId);
}
