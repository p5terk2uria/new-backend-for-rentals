package system.services.serviceproviders;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.services.bidorder.BidStatus;
import system.services.bidorder.PlaceBidRequest;
import system.services.bidorder.ServiceBidSearchRequest;
import system.services.config.ApiResponse;
import system.services.config.BaseController;
import system.services.serviceproviders.dto.ServiceProviderResponse;
import system.services.serviceproviders.enums.AvailableStatus;
import system.services.serviceproviders.dto.ServiceProviderRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/services/provider")
public class ServiceProviderController extends BaseController {

    private final ServiceProviderService provideService;

    @PostMapping("/create-provider")
    public ResponseEntity<ApiResponse<?>> createServiceProvider(
            @RequestBody ServiceProviderRequest request
    ) {
       String response =  provideService.addServiceProvider(request);
        return ResponseEntity.ok(success("service provider onboarded successfully",response));
    }

    @GetMapping("/get-providers-by-service")
    public ResponseEntity<ApiResponse<?>> searchProvidersByService(
            @RequestParam String serviceId,
            Pageable pageable
    ) {
        var response = provideService.getAllServiceProvidersByService(serviceId, pageable);
        return ResponseEntity.ok(success("Services fetched", response));
    }

    @PatchMapping("/update-service-provider-availability")
    public ResponseEntity<ApiResponse<?>> updateServiceProviderAvailability(
            @RequestParam String serviceId,
            @RequestParam AvailableStatus availableStatus
    ) {
        provideService.updateServiceProvideAvailability(serviceId, availableStatus);
        return ResponseEntity.ok(success("update success"));
    }


    @PostMapping("/place-bid")
    public ResponseEntity<ApiResponse<?>> placeBid(
            @RequestBody PlaceBidRequest request) {

        var response  = provideService.placeBid(request);
        return ResponseEntity.ok(success(response));
    }

    @GetMapping("/search-bids")
    public ResponseEntity<ApiResponse<?>> searchBids (
            @RequestParam (required = false) String orderId,
            @RequestParam (required = false) String serviceProviderId,
            @RequestParam (required = false) BidStatus  bidStatus,
            Pageable pageable) {

        ServiceBidSearchRequest request = ServiceBidSearchRequest.builder()
                .orderId(orderId)
                .serviceProviderId(serviceProviderId)
                .status(bidStatus)
                .build();

        return ResponseEntity.ok(success(provideService.searchBids(request,pageable)));


    }


    @GetMapping("/get-service-provider-by-id")
    public ServiceProviderResponse getServiceProviderById(@RequestParam String id) {
        return provideService.findServiceProviderById(id);
    }

    @GetMapping("/get-service-provider-by-orderTrackingId")
    public ServiceProviderResponse getServiceProviderByOrderTrackingId(
            @RequestParam String orderTrackingId
    ) {
        return provideService.findServiceProviderByOrderTrackingId(orderTrackingId);
    }

    @PatchMapping("/feign/update-service-provider-availability")
    public void updateServiceProviderStatus(
            @RequestParam String serviceId,
            @RequestParam AvailableStatus availableStatus
    ) {
        provideService.updateServiceProvideAvailability(serviceId, availableStatus);
    }




}
