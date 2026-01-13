package productservice.bookings;

import jakarta.persistence.*;
import lombok.*;
import productservice.bookings.dto.BookingStatus;
import productservice.room.Room;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private LocalDate bookingDate;

    private String orderTrackingId;

    private String houseBill;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

}
