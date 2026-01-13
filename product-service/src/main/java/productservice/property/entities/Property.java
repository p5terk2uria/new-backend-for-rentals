package productservice.property.entities;

import jakarta.persistence.*;
import lombok.*;

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

    private String ownerId;

    private String ownerName;

    private String propertyName;

    private String ownerEmail;

    private String propertyLocation;

    private String houseDescription;

    private int noOfRooms;

    private String videoLink;


}
