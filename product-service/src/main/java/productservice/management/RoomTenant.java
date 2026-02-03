package productservice.management;

import jakarta.persistence.*;
import lombok.*;
import productservice.room.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoomTenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String propertyId;

    private String userId;

    private String userName;

    private String phoneNumber;

    private String email;

    private String roomId;

    private LocalDate leaseDate;

    private BigDecimal balance;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

}
