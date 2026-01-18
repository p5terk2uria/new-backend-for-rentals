package authentication_service.user.permissions;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permissions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String serviceName;

    private String path;

    private String method;

    private String permissions;
}
