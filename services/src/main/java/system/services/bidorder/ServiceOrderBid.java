package system.services.bidorder;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ServiceOrderBid {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String orderId;

    private String serviceProviderId;

    private BigDecimal bidAmount;

    private String message;

    @Enumerated(EnumType.STRING)
    private BidStatus status;

    private LocalDate createdAt;


}
