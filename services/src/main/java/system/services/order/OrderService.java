package system.services.order;

import org.springframework.data.domain.Pageable;
import system.services.order.dto.*;

import java.util.List;


public interface OrderService {

   String requestService(RequestServiceRequest request);

   String attachOrderToServiceProvider(AttachOrderRequest request);

   List<OrderServiceResponse> searchOrderService(OrderSearchRequest request, Pageable pageable);

   List<AdminOrderResponse> searchAdminOrderService(OrderSearchRequest request, Pageable pageable);
}
