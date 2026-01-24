package system.services.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderServiceRepository extends JpaRepository<ServiceOrder, String>, JpaSpecificationExecutor<ServiceOrder> {
}
