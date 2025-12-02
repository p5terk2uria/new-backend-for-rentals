package productservice.property.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyBills {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String  id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Property property;

    private BigDecimal houseBill;

    private BigDecimal waterBill;

    private BigDecimal trashBill;

    private BigDecimal maintenanceBill;

    private BigDecimal otherBills;
}
