package system.services.serviceproviders.enums;

import lombok.Builder;

@Builder(toBuilder = true)
public record ServiceProviderResponse (

        String id,

        String name,

        String phoneNumber,

        String serviceName,

        String serviceId,

        String location,

        AvailableStatus availableStatus
) {
}
