package productservice.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CallBack {
    @Id
    private String orderTrackingId;

    private String orderNotificationType;

    private String orderMerchantReference;

}
