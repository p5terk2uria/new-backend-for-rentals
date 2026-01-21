package system.services.serviceproviders;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import system.services.feignclients.authentication.AuthenticationClient;
import system.services.feignclients.authentication.UserData;
import system.services.serviceproviders.enums.AvailableStatus;
import system.services.serviceproviders.enums.ServiceProviderRequest;
import system.services.serviceproviders.enums.ServiceProviderResponse;
import system.services.services.PropertyServiceRepository;
import system.services.services.Services;

@Service
@RequiredArgsConstructor
public class ServiceProvidersServiceImpl implements ServiceProviderService {

    private final AuthenticationClient authenticationClient;

    private final PropertyServiceRepository serviceRepository;

    private final ServiceProviderRepository providerRepository;

    /**
     * @param request
     */
    @Override
    public void addServiceProvider(ServiceProviderRequest request) {

        UserData userData = authenticationClient.getUserById(request.id());

        if (userData == null) {
           throw new RuntimeException("user not found with this id");
        }

        Services services = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new RuntimeException("Service not found with this id"));

        ServiceProvider provider = ServiceProvider.builder()
                .id(userData.id())
                .name(userData.firstName() + " " + userData.lastName())
                .phoneNumber(userData.phoneNumber())
                .serviceId(services.getId())
                .location(userData.city())
                .serviceName(services.getServiceName().trim())
                .availability(AvailableStatus.AVAILABLE)
                .build();

        providerRepository.save(provider);
    }

    /**
     * @param service
     * @return
     */
    @Override
    public Page<ServiceProviderResponse> getAllServiceProvidersByService(String service) {
        return null;
    }
}
