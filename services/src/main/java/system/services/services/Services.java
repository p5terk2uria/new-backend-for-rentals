package system.services.services;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String serviceName;

    private String description;

}
