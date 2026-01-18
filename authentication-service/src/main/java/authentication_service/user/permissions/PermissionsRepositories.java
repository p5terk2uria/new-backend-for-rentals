package authentication_service.user.permissions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionsRepositories extends JpaRepository <Permissions, String>{

    List<Permissions> findByServiceName(String serviceName);

}
