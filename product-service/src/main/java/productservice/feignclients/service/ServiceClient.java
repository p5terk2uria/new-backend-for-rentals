package productservice.feignclients.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "services", url = "http://localhost:8334")
public interface ServiceClient {

    @GetMapping("/api/services/provider/get-service-provider-by-id")
    ServiceProviderResponse getProviderById(@RequestParam("id") String id);

    @GetMapping("/api/services/provider/get-service-provider-by-orderTrackingId")
    ServiceProviderResponse getProviderByOrderTrackingId(@RequestParam("orderTrackingId") String orderTrackingId);

    @PatchMapping("/api/services/provide/feign/update-service-provider-availability")
    void updateServiceProviderStatus(@RequestParam("serviceId") String serviceId,
                                     @RequestParam("availableStatus") AvailableStatus availableStatus);

    @GetMapping("/api/services/order/find-by-order-trackingId")
    AdminOrderResponse getOrderByOrderTrackingId(@RequestParam("orderTrackingId") String orderTrackingId);

    @PutMapping("/api/services/order/update-order-payment-status")
    void updateOrderPaymentStatus(@RequestParam("orderId") String orderId,
                                  @RequestParam("paymentStatus") OrderPaymentStatus paymentStatus);
}

