package productservice.feignclients.service;

import java.math.BigDecimal;

public record ServiceProviderResponse(
        String id,

        String name,

        String phoneNumber,

        String serviceName,

        String serviceId,

        String location,

        BigDecimal balance,

        String orderTrackingId,

        String email,

        AvailableStatus availableStatus
) {
}
