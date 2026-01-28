package productservice.feignclients.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "services", url = "http://localhost:8334")
public interface ServiceClient {

    @GetMapping("/api/services/provider/get-service-provider-by-id")
    ServiceProviderResponse getProviderById(@RequestParam("id") String id);

    @GetMapping("/api/services/provider/get-service-provider-by-orderTrackingId")
    ServiceProviderResponse getProviderByOrderTrackingId(@RequestParam("orderTrackingId") String orderTrackingId);

    @PatchMapping("/feign/update-service-provider-availability")
    void updateServiceProviderStatus(@RequestParam("serviceId") String serviceId,
                                     @RequestParam("availableStatus") AvailableStatus availableStatus);

}

