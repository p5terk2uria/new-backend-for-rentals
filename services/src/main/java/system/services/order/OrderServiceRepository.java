package system.services.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrderServiceRepository extends JpaRepository<ServiceOrder, String>,
        JpaSpecificationExecutor<ServiceOrder> {

    Optional<ServiceOrder> findServiceOrderByOrderTrackingId(String oderTrackingId);
}
