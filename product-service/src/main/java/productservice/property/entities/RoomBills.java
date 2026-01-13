package productservice.property.entities;

import jakarta.persistence.*;
import lombok.*;
import productservice.room.Room;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomBills {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String  id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    private String houseBill;

    private String waterBill;

    private String trashBill;

    private String maintenanceBill;

    private String otherBills;
}
