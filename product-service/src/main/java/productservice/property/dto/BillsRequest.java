package productservice.property.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Description;


import java.math.BigDecimal;

@Description("The request dto used in saving the bill request types eg monthly bill")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BillsRequest(

        @NotNull(message = "House bill cannot be null")
        BigDecimal houseBill,

        BigDecimal waterBill,

        BigDecimal trashBill,

        BigDecimal maintenanceBill,

        BigDecimal otherBills



) {
}
