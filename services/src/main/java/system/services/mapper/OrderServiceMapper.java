package system.services.mapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import system.services.order.ServiceOrder;
import system.services.order.dto.AdminOrderResponse;
import system.services.order.dto.OrderServiceResponse;
import system.services.order.dto.RequestServiceRequest;
import system.services.order.dto.RequestServiceResponse;
import system.services.order.enums.OrderStatus;

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
                serviceOrder.getOrderId(),
                null,
                serviceOrder.getDescription(),
                serviceOrder.getOrderStatus()

        );
    }

    public OrderServiceResponse toOrderResponse(ServiceOrder serviceOrder) {

        BigDecimal deductedBudget = serviceOrder.getBudget()
                .subtract(serviceOrder.getBudget().multiply(providerCommission));

        return OrderServiceResponse.builder()
                .id(serviceOrder.getId())
                .orderId(serviceOrder.getOrderId())
                .orderStatus(serviceOrder.getOrderStatus())
                .serviceName(serviceOrder.getServiceName())
                .serviceProviderId(serviceOrder.getServiceProviderId())
                .userId(serviceOrder.getUserId())
                .description(serviceOrder.getDescription())
                .budget(deductedBudget)
                .build();
    }
    public AdminOrderResponse toAdminOrderResponse (ServiceOrder serviceOrder) {

        return AdminOrderResponse.builder()
                .id(serviceOrder.getId())
                .orderId(serviceOrder.getOrderId())
                .orderStatus(serviceOrder.getOrderStatus())
                .serviceName(serviceOrder.getServiceName())
                .serviceProviderId(serviceOrder.getServiceProviderId())
                .userId(serviceOrder.getUserId())
                .description(serviceOrder.getDescription())
                .budget(serviceOrder.getBudget())
                .build();


    }
}
