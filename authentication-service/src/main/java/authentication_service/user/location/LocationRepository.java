package authentication_service.user.location;

import authentication_service.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface LocationRepository extends JpaRepository<LocationDetails, String> {

    Optional<LocationDetails> findByUser(User user);

}
