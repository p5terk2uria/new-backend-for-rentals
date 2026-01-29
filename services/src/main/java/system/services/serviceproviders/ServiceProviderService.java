package system.services.serviceproviders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import system.services.bidorder.PlaceBidRequest;
import system.services.bidorder.ServiceBidSearchRequest;
import system.services.bidorder.ServiceBidSearchResponse;
import system.services.serviceproviders.enums.AvailableStatus;
import system.services.serviceproviders.dto.ServiceProviderRequest;
import system.services.serviceproviders.dto.ServiceProviderResponse;


public interface ServiceProviderService {

    void addServiceProvider(ServiceProviderRequest request);

    Page<ServiceProviderResponse> getAllServiceProvidersByService(String service, Pageable pageable);

    String placeBid(PlaceBidRequest request);

    Page<ServiceBidSearchResponse> searchBids(ServiceBidSearchRequest request, Pageable pageable);

    void updateServiceProvideAvailability (String serviceProvideId, AvailableStatus availableStatus);

    ServiceProviderResponse findServiceProviderById(String providerId);

    ServiceProviderResponse findServiceProviderByOrderTrackingId(String orderTrackingId);
}
