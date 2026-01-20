package productservice.room;

import jakarta.persistence.*;
import lombok.*;
import productservice.bookings.dto.BookingStatus;
import productservice.property.entities.Property;
import productservice.property.entities.RoomBills;
import productservice.property.enums.HouseType;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Property property;

    @Enumerated(EnumType.STRING)
    private HouseType houseType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RoomBills roomBills;

    private String houseBill;

    private boolean vacant;

    private Set<String> imageUrls;

    private String videoUrl;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
}
