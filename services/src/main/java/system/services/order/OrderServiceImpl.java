package system.services.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import system.services.mapper.OrderServiceMapper;
import system.services.order.dto.AttachOrderRequest;
import system.services.order.dto.RequestServiceRequest;
import system.services.serviceproviders.ServiceProvider;
import system.services.serviceproviders.ServiceProviderRepository;
import system.services.serviceproviders.enums.AvailableStatus;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    /**
     *
     */
    private final OrderServiceMapper orderServiceMapper;
    private final OrderServiceRepository orderServiceRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    @Override
    public String requestService(RequestServiceRequest request) {

        String orderId = "ORDER" + System.currentTimeMillis();

        var serviceOrder = orderServiceMapper.toServiceOrder(request);
        serviceOrder.setOrderId(orderId);
        serviceOrder.setDateRequested(LocalDate.now());

        if (request.serviceProviderId() != null) {
            serviceOrder.setServiceProviderId(request.serviceProviderId());
        }
        orderServiceRepository.save(serviceOrder);

        return serviceOrder.getOrderId();
    }


    @Override
    public String attachOrderToServiceProvider(AttachOrderRequest request) {

        ServiceProvider serviceProvider = serviceProviderRepository.findById(request.serviceProvideId())
                .orElseThrow(() -> new RuntimeException("Service provider with this id not found"));

        if (serviceProvider.getAvailability() != AvailableStatus.AVAILABLE) {
            throw new RuntimeException("Service provider selected not available");
        }
        ServiceOrder order = orderServiceRepository.findById(request.orderId())
                .orElseThrow(() -> new RuntimeException("Order with the given id not available"));

        order.setServiceProviderId(request.serviceProvideId());
        orderServiceRepository.save(order);
        return "success";
    }
}
