package system.services.serviceproviders.enums;

import lombok.Builder;

@Builder(toBuilder = true)
public record ServiceProviderRequest (

        String id,

        String name,

        String phoneNumber,

        String serviceName,

        String serviceId,

        String location,

        AvailableStatus availableStatus
){

}
