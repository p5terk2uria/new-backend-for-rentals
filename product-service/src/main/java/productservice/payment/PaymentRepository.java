package productservice.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentConfirmation, String> {

    Optional<PaymentConfirmation> findByOrderTrackingId (String orderTrackingId);
}
