package productservice.property.entities;

import jakarta.persistence.*;
import lombok.*;
import productservice.property.enums.AmenityType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyAmenities {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private AmenityType amenityType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Property property;
}

