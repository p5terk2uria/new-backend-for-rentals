package system.services.order;

import system.services.order.dto.AttachOrderRequest;
import system.services.order.dto.RequestServiceRequest;


public interface OrderService {

   String requestService(RequestServiceRequest request);


   String attachOrderToServiceProvider(AttachOrderRequest request);
}
