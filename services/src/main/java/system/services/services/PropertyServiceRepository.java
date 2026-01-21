package system.services.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyServiceRepository extends JpaRepository<Services, String>,
        JpaSpecificationExecutor<Services> {
}
