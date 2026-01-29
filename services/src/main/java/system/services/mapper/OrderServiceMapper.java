package system.services.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import system.services.bidorder.ServiceBidSearchResponse;
import system.services.bidorder.ServiceOrderBid;
import system.services.order.ServiceOrder;
import system.services.order.dto.AdminOrderResponse;
import system.services.order.dto.OrderServiceResponse;
import system.services.order.dto.RequestServiceRequest;
import system.services.order.dto.RequestServiceResponse;
import system.services.order.enums.OrderStatus;
import system.services.serviceproviders.ServiceProvider;
import system.services.serviceproviders.dto.ServiceProviderResponse;

import java.math.BigDecimal;

@Component
public class OrderServiceMapper {

    @Value("${platform.commission.provider}")
    private BigDecimal providerCommission;

    public ServiceOrder toServiceOrder(RequestServiceRequest request) {
        return ServiceOrder.builder()
                .userId(request.userId())
                .serviceId(request.serviceId())
                .serviceProviderId(request.serviceProviderId())
                .dateRequested(request.dateRequested())
                .expectedDeadline(request.expectedDeadline())
                .budget(request.budget())
                .description(request.description())
                .orderStatus(OrderStatus.ACTIVE)
                .location(request.location())
                .build();
    }

    public RequestServiceResponse toServiceResponse(ServiceOrder serviceOrder) {
        return new RequestServiceResponse(
                serviceOrder.getId(),
                serviceOrder.getUserId(),
                serviceOrder.getServiceId(),
                serviceOrder.getServiceProviderId(),
                serviceOrder.getDateRequested(),
                serviceOrder.getExpectedDeadline(),
                serviceOrder.getOrderTrackingId(),
                null,
                serviceOrder.getDescription(),
                serviceOrder.getOrderStatus()

        );
    }

    public ServiceProviderResponse toServiceProviderResponse(ServiceProvider provider) {
        return ServiceProviderResponse.builder()
                .id(provider.getId())
                .name(provider.getName())
                .phoneNumber(provider.getPhoneNumber())
                .serviceName(provider.getServiceName())
                .serviceId(provider.getServiceId())
                .location(provider.getLocation())
                .balance(provider.getBalance())
                .orderTrackingId(provider.getOrderTrackingId())
                .email(provider.getEmail())
                .availableStatus(provider.getAvailability())
                .build();
    }

    public OrderServiceResponse toOrderResponse(ServiceOrder serviceOrder) {

        BigDecimal deductedBudget = serviceOrder.getBudget()
                .subtract(serviceOrder.getBudget().multiply(providerCommission));

        return OrderServiceResponse.builder()
                .id(serviceOrder.getId())
                .orderTrackingId(serviceOrder.getOrderTrackingId())
                .dateRequested(serviceOrder.getDateRequested().toString())
                .expectedDeadline(serviceOrder.getExpectedDeadline().toString())
                .orderStatus(serviceOrder.getOrderStatus())
                .serviceName(serviceOrder.getServiceName())
                .serviceProviderId(serviceOrder.getServiceProviderId())
                .userId(serviceOrder.getUserId())
                .description(serviceOrder.getDescription())
                .location(serviceOrder.getLocation())
                .orderPaymentStatus(serviceOrder.getPaymentStatus())
                .budget(deductedBudget)
                .build();
    }

    public AdminOrderResponse toAdminOrderResponse(ServiceOrder serviceOrder) {

        return AdminOrderResponse.builder()
                .id(serviceOrder.getId())
                .orderTrackingId(serviceOrder.getOrderTrackingId())
                .orderStatus(serviceOrder.getOrderStatus())
                .serviceName(serviceOrder.getServiceName())
                .serviceProviderId(serviceOrder.getServiceProviderId())
                .userId(serviceOrder.getUserId())
                .description(serviceOrder.getDescription())
                .budget(serviceOrder.getBudget())
                .build();


    }

    public ServiceBidSearchResponse toBidSearchResponse (ServiceOrderBid serviceOrderBid) {

        return ServiceBidSearchResponse.builder()
                .id(serviceOrderBid.getId())
                .orderId(serviceOrderBid.getOrderId())
                .message(serviceOrderBid.getMessage())
                .bidStatus(serviceOrderBid.getStatus())
                .bidedAt(serviceOrderBid.getCreatedAt())
                .build();
    }
}
