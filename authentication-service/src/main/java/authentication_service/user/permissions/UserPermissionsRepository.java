package authentication_service.user.permissions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPermissionsRepository extends JpaRepository<UserPermissions, String> {

    List<UserPermissions> findByUserId(String userId);

    boolean existsByUserIdAndPermission(String userId, String permission);

    
}
