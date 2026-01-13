package productservice.visit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RequestVisitRepository extends JpaRepository<RequestVisit, String> {

    Optional<RequestVisit> findByOrderTrackingId(String orderTrackingId);

}
