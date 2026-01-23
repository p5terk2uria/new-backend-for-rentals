package system.services.services;


import jakarta.persistence.*;
import lombok.*;
import system.services.services.dto.AttendanceStatus;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String serviceName;

    private String description;

    private int dateRequested;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;

}
