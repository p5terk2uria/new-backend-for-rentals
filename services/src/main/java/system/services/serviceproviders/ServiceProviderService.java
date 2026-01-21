package system.services.serviceproviders;

import org.springframework.data.domain.Page;
import system.services.serviceproviders.enums.ServiceProviderRequest;
import system.services.serviceproviders.enums.ServiceProviderResponse;


public interface ServiceProviderService {

    void addServiceProvider(ServiceProviderRequest request);

    Page<ServiceProviderResponse> getAllServiceProvidersByService(String service);
}
