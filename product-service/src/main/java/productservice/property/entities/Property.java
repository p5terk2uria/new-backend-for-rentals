package productservice.property.entities;

import jakarta.persistence.*;
import lombok.*;
import productservice.property.enums.HouseType;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    //TODO ---> this will have a many to one relationship with the landlord
    private String ownerId;

    private String ownerName;

    private String propertyName;

    private String ownerEmail;

    private String propertyLocation;

    @Enumerated(EnumType.STRING)
    private HouseType houseType;

    private String videoLink;


}
