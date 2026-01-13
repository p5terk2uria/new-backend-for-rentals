package productservice.payment;

import jakarta.persistence.*;
import lombok.*;
import productservice.bookings.BookRoom;
import productservice.payment.enums.PaymentReason;
import productservice.payment.enums.PaymentStatus;
import productservice.visit.RequestVisit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentConfirmation {

    @Id
    private String orderTrackingId;

    private String merchantReference;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    private PaymentReason paymentReason;

    private String referenceId;

    @OneToOne
    private RequestVisit requestVisit;

    @OneToOne
    private BookRoom bookRoom;



}


