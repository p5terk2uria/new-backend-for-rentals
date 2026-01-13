package productservice.visit;

import jakarta.persistence.*;
import lombok.*;
import productservice.room.Room;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;

    private String orderTrackingId;

    private String tenantName;

    private LocalDate visitingDate;

    private LocalTime visitingTime;

    private int noOfVisitors;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @Column(length = 1000)
    private String notes;


    public enum RequestStatus {
        NOT_CONFIRMED,
        VISITED,
        PENDING,
        CANCELLED

}}


