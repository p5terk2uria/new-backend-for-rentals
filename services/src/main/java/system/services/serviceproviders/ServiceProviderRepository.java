package system.services.serviceproviders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider,String> {

    Page<ServiceProvider> findByServiceId(String serviceId, Pageable pageable);

   Optional<ServiceProvider> findByOrderTrackingId(String orderTrackingId);

}
