package authentication_service.user.location;

import authentication_service.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String country;

    private String countryCode;

    private String state;

    private String city;

    private String postalCode;

    private String zipCode;

    private String line1;

    private String line2;

    @OneToOne
    private User user;

}
