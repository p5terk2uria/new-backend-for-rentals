package productservice.management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTenantRepository extends JpaRepository<RoomTenant, String>,
        JpaSpecificationExecutor<RoomTenant> {

    boolean existsByUserIdAndRoomId(String userId, String roomId);

    Optional<RoomTenant> findRoomTenantByUserIdAndRoomId(String userId, String roomId);
}