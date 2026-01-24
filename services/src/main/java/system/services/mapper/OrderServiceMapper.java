package system.services.mapper;

import org.springframework.stereotype.Component;
import system.services.order.ServiceOrder;
import system.services.order.dto.RequestServiceRequest;
import system.services.order.dto.RequestServiceResponse;
import system.services.order.enums.OrderStatus;

@Component
public class OrderServiceMapper {

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
}
