package system.services.serviceproviders;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import system.services.serviceproviders.enums.AvailableStatus;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ServiceProvider {

    @Id
    private String id;

    private String name;

    private String phoneNumber;

    private String email;

    private String serviceName;

    private String serviceId;

    private String location;

    private BigDecimal balance;

    private String orderTrackingId;

    @Enumerated(EnumType.STRING)
    private AvailableStatus availability;


}
