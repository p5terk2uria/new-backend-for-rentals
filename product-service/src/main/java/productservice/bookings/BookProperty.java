package productservice.bookings;
import jakarta.persistence.*;
import lombok.*;
import productservice.property.entities.Property;
import productservice.visits.RequestVisit;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String tenantName;

    private LocalDate bookingDate;

    private RequestVisit.RequestStatus status;

    @OneToOne
    private Property property;

    private String roomNo;

    public enum RequestStatus {
        NOT_CONFIRMED,
        VISITED,
        PENDING,
        CANCELLED

    }
}
