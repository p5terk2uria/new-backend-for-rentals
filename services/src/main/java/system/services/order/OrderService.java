package system.services.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import system.services.order.dto.*;
import system.services.order.enums.OrderPaymentStatus;
import system.services.order.enums.OrderStatus;

import java.util.List;


public interface OrderService {

    String requestService(RequestServiceRequest request);

    String attachOrderToServiceProvider(AttachOrderRequest request);

    List<OrderServiceResponse> searchOrderService(OrderSearchRequest request, Pageable pageable);

    List<AdminOrderResponse> searchAdminOrderService(OrderSearchRequest request, Pageable pageable);

    AdminOrderResponse getServiceOrderByOrderTrackingId(String orderId);

    Page<AdminOrderResponse> getAllOrders(Pageable pageable);

    void updateOrderPaymentStatus(String orderTrackingId, OrderPaymentStatus paymentStatus);

    void updateOrderStatus(String orderId, OrderStatus desiredStatus);
}
