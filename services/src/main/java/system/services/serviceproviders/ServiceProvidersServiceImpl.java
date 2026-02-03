package system.services.serviceproviders;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import system.services.bidorder.*;
import system.services.feignclients.authentication.AuthenticationClient;
import system.services.feignclients.authentication.UserData;
import system.services.mapper.OrderServiceMapper;
import system.services.order.OrderServiceRepository;
import system.services.serviceproviders.enums.AvailableStatus;
import system.services.serviceproviders.enums.DomainRoles;
import system.services.serviceproviders.dto.ServiceProviderRequest;
import system.services.serviceproviders.dto.ServiceProviderResponse;
import system.services.services.PropertyServiceRepository;
import system.services.services.Services;
import system.services.specifications.ServiceBidSpecification;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ServiceProvidersServiceImpl implements ServiceProviderService {

    private final AuthenticationClient authenticationClient;

    private final PropertyServiceRepository serviceRepository;

    private final ServiceProviderRepository providerRepository;

    private final OrderServiceRepository orderServiceRepository;

    private final ServiceOrderBidRepository bidRepository;

    private final OrderServiceMapper orderServiceMapper;

    /**
     *
     */
    @Override
    public String addServiceProvider(ServiceProviderRequest request) {

        UserData userData = authenticationClient.getUserById(request.userId());

        if (userData == null) {
            throw new RuntimeException("user not found with this id");
        }
        if (userData.role() != DomainRoles.SERVICE_PROVIDER) {
            throw new RuntimeException("Service provider must have service provider role");
        }

        Services services = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new RuntimeException("Service not found with this id"));

        String orderTrackingId = "ORDER"+ System.currentTimeMillis();

        ServiceProvider provider = ServiceProvider.builder()
                .id(userData.id())
                .name(userData.firstName() + " " + userData.lastName())
                .phoneNumber(userData.phoneNumber())
                .email(userData.emailAddress())
                .serviceId(services.getId())
                .location(userData.city())
                .serviceName(services.getServiceName().trim())
                .balance(BigDecimal.ZERO)
                .availability(AvailableStatus.PENDING)
                .orderTrackingId(orderTrackingId)
                .build();

       return providerRepository.save(provider).getId();

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
    public String placeBid(PlaceBidRequest request) {

        ServiceProvider provider = providerRepository.findById(request.serviceProviderId())
                .orElseThrow(() -> new RuntimeException("Service provider not found for this id"));

        if (provider.getAvailability() != AvailableStatus.AVAILABLE) {
            throw new RuntimeException("Provider not in available state");

        }
        orderServiceRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order not found for this id"));

        bidRepository.findByOrderIdAndServiceProviderId(request.orderId(), request.serviceProviderId())
                .ifPresent(b -> {
                    throw new RuntimeException("You already placed a bid on this order");
                });

        ServiceOrderBid bid = new ServiceOrderBid();
        bid.setOrderId(request.orderId());
        bid.setServiceProviderId(request.serviceProviderId());
        bid.setMessage(request.message());
        bid.setStatus(BidStatus.PENDING);
        bid.setCreatedAt(LocalDate.now());
        bidRepository.save(bid);
        return bid.getId();

    }

    @Override
    public Page<ServiceBidSearchResponse> searchBids(ServiceBidSearchRequest request, Pageable pageable) {

        var spec = ServiceBidSpecification.search(request);

        return bidRepository.findAll(spec,pageable)
                .map(orderServiceMapper::toBidSearchResponse);

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

    @Override
    public ServiceProviderResponse findServiceProviderById(String providerId) {

        var serviceProvider = providerRepository.findById(providerId)
                .orElseThrow(()-> new RuntimeException("Service provider not found for this id"));
        return orderServiceMapper.toServiceProviderResponse(serviceProvider);
    }

    @Override
    public ServiceProviderResponse findServiceProviderByOrderTrackingId(String orderTrackingId) {

        var serviceProvider = providerRepository.findByOrderTrackingId(orderTrackingId)
                .orElseThrow(() -> new RuntimeException("service provider not found for this id"));
        return orderServiceMapper.toServiceProviderResponse(serviceProvider);

    }



}
