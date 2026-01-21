package system.services.services.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record ServiceRequest (

        String serviceName,

        String description
) {
}
