package system.services.serviceproviders.dto;

import lombok.Builder;
import system.services.serviceproviders.enums.AvailableStatus;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record ServiceProviderResponse (

        String id,

        String name,

        String phoneNumber,

        String serviceName,

        String serviceId,

        String location,

        BigDecimal balance,

        AvailableStatus availableStatus
) {
}
