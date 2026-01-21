package system.services.services;

import lombok.RequiredArgsConstructor;
import system.services.services.dto.ServiceRequest;
import system.services.services.dto.ServiceResponse;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyServiceRepository serviceRepository;

    /**
     *
     */
    @Override
    public void createService(ServiceRequest request) {

        Services service = Services.builder()
                .serviceName(request.serviceName())
                .description(request.description())
                .build();
        serviceRepository.save(service);
    }

    /**
     *
     */
    @Override
    public List<ServiceResponse> getAllServices() {

        List<Services> services = serviceRepository.findAll();

        return services.stream().map(
                response -> ServiceResponse.builder()
                        .serviceId(response.getId())
                        .serviceName(response.getServiceName())
                        .serviceDescription(response.getDescription())
                        .build()
        ).toList();
    }
}
