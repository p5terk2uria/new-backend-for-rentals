package system.services.serviceproviders;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import system.services.feignclients.authentication.AuthenticationClient;
import system.services.feignclients.authentication.UserData;
import system.services.serviceproviders.enums.AvailableStatus;
import system.services.serviceproviders.enums.DomainRoles;
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
     *
     */
    @Override
    public void addServiceProvider(ServiceProviderRequest request) {

        UserData userData = authenticationClient.getUserById(request.userId());

        if (userData == null) {
            throw new RuntimeException("user not found with this id");
        }
        if (userData.role() != DomainRoles.SERVICE_PROVIDER) {
            throw new RuntimeException("Service provider must have service provider role");
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
     *
     */
    @Override
    public Page<ServiceProviderResponse> getAllServiceProvidersByService(
            String serviceId,
            Pageable pageable
    ) {

        return providerRepository.findByServiceId(serviceId, pageable)
                .map(provider -> ServiceProviderResponse.builder()
                        .id(provider.getId())
                        .name(provider.getName())
                        .phoneNumber(provider.getPhoneNumber())
                        .serviceName(provider.getServiceName())
                        .serviceId(provider.getServiceId())
                        .location(provider.getLocation())
                        .availableStatus(provider.getAvailability())
                        .build()
                );
    }


    @Override
    public void updateServiceProvideAvailability(String serviceProvideId, AvailableStatus availableStatus) {

        ServiceProvider provider = providerRepository.findById(serviceProvideId)
                .orElseThrow(() -> new RuntimeException("Service provider not found for this id"));

        AvailableStatus currentStatus = provider.getAvailability();

        if (currentStatus == availableStatus) {
            throw new RuntimeException("Service provider already in this status");
        }
        provider.setAvailability(availableStatus);
        providerRepository.save(provider);

    }

}
