package system.services.serviceproviders.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;
import system.services.serviceproviders.enums.AvailableStatus;

@Builder(toBuilder = true)
public record ServiceProviderRequest (

        String userId,

        @Hidden
        String name,

        @Hidden
        String phoneNumber,

        @Hidden
        String serviceName,

        String serviceId,

        @Hidden
        String location,

        @Hidden
        AvailableStatus availableStatus
){

}
