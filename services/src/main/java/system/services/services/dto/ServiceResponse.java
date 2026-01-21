package system.services.services.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record ServiceResponse(

        String serviceId,

        String serviceName,

        String serviceDescription
) {
}
