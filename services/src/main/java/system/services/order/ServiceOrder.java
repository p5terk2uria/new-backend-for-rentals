package system.services.order;

import jakarta.persistence.*;
import lombok.*;
import system.services.order.enums.OrderPaymentStatus;
import system.services.order.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ServiceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private String serviceId;

    private String serviceName;

    private String serviceProviderId;

    private LocalDate dateRequested = LocalDate.now();

    private LocalDate expectedDeadline;

    private String orderTrackingId;

    private String location;

    private BigDecimal budget;

    private String description;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private OrderPaymentStatus paymentStatus;

}
