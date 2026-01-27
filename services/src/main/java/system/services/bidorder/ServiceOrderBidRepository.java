package system.services.bidorder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceOrderBidRepository extends JpaRepository<ServiceOrderBid, String> {

    List<ServiceOrderBid> findByOrderId(String orderId);

    Optional<ServiceOrderBid> findByOrderIdAndServiceProviderId(
            String orderId, String serviceProviderId
    );



}
