package system.services.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import system.services.specifications.ServiceOrderSpecification;
import system.services.mapper.OrderServiceMapper;
import system.services.order.dto.*;
import system.services.order.enums.OrderPaymentStatus;
import system.services.order.enums.OrderStatus;
import system.services.serviceproviders.ServiceProvider;
import system.services.serviceproviders.ServiceProviderRepository;
import system.services.serviceproviders.enums.AvailableStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderServiceMapper orderServiceMapper;
    private final OrderServiceRepository orderServiceRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS =
            Map.of(OrderStatus.IN_PROGRESS, Set.of(OrderStatus.EXECUTED, OrderStatus.CANCELLED),
                    OrderStatus.ACTIVE, Set.of(OrderStatus.IN_PROGRESS, OrderStatus.EXPIRED, OrderStatus.CANCELLED),
                    OrderStatus.PENDING, Set.of(OrderStatus.ACTIVE, OrderStatus.EXPIRED, OrderStatus.CANCELLED)
            );

    @Override
    public String requestService(RequestServiceRequest request) {

        String orderId = "ORDER" + System.currentTimeMillis();

        var serviceOrder = orderServiceMapper.toServiceOrder(request);
        serviceOrder.setOrderTrackingId(orderId);
        serviceOrder.setPaymentStatus(OrderPaymentStatus.NOT_PAID);
        serviceOrder.setDateRequested(LocalDate.now());

        if (request.serviceProviderId() != null) {
            serviceOrder.setServiceProviderId(request.serviceProviderId());
        }
        serviceOrder.setOrderStatus(OrderStatus.ACTIVE);
        orderServiceRepository.save(serviceOrder);
        return orderServiceRepository.save(serviceOrder).getOrderTrackingId();
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
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        orderServiceRepository.save(order);
        return "success";
    }


    @Override
    public List<OrderServiceResponse> searchOrderService(OrderSearchRequest request, Pageable pageable) {

        var spec = ServiceOrderSpecification.search(request);

        return orderServiceRepository.findAll(spec, pageable)
                .map(orderServiceMapper::toOrderResponse).getContent();

    }

    @Override
    public List<AdminOrderResponse> searchAdminOrderService(OrderSearchRequest request, Pageable pageable) {

        var spec = ServiceOrderSpecification.search(request);

        return orderServiceRepository.findAll(spec, pageable)
                .map(orderServiceMapper::toAdminOrderResponse).getContent();

    }

    @Override
    public AdminOrderResponse getServiceOrderByOrderTrackingId(String orderTrackingId) {

        return orderServiceMapper.toAdminOrderResponse(orderServiceRepository.
                findServiceOrderByOrderTrackingId(orderTrackingId)
                .orElseThrow(() -> new RuntimeException("No order found with this orderTrackingId")));
    }

    @Override
    public Page<AdminOrderResponse> getAllOrders(Pageable pageable) {

        Page<ServiceOrder> orders = orderServiceRepository.findAll(pageable);
        return orders.map(
                orderServiceMapper::toAdminOrderResponse);
    }

    @Override
    public void updateOrderPaymentStatus(String orderTrackingId, OrderPaymentStatus paymentStatus) {

        ServiceOrder order = orderServiceRepository
                .findServiceOrderByOrderTrackingId(orderTrackingId)
                .orElseThrow(() -> new RuntimeException("Order not found for this orderTracking id"));

        if (order.getPaymentStatus() == OrderPaymentStatus.PAID) {
            return;
        }
        if (paymentStatus == OrderPaymentStatus.NOT_PAID) {
            order.setPaymentStatus(OrderPaymentStatus.PAID);
            order.setOrderStatus(OrderStatus.ACTIVE);
        }
        orderServiceRepository.save(order);
        throw new RuntimeException("Illegal payment status passed");

    }

    @Override
    public void updateOrderStatus(String orderId, OrderStatus desiredStatus) {

        ServiceOrder order = orderServiceRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("no order found for this id"));

        if(!VALID_TRANSITIONS.getOrDefault(order.getOrderStatus(), Collections.emptySet()).contains(desiredStatus)) {
            throw new RuntimeException("Invalid order status status transition");
        }

        order.setOrderStatus(desiredStatus);
        orderServiceRepository.save(order);
    }


}
