package productservice.room.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public record BillsRequest(
         String houseBill,

         String waterBill,

         String trashBill,

         String maintenanceBill,

         String otherBills
) {
}
