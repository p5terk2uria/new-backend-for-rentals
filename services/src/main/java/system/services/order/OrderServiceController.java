package system.services.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import system.services.config.ApiResponse;
import system.services.config.BaseController;
import system.services.order.dto.AttachOrderRequest;
import system.services.order.dto.RequestServiceRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/services/order")
public class OrderServiceController extends BaseController {

    private final OrderService orderService;

    @PostMapping("/service")
    public ResponseEntity<ApiResponse<?>> orderService(RequestServiceRequest request) {

        String response = orderService.requestService(request);
        return ResponseEntity.ok(success("success", response));
    }

    @PostMapping("/attach-order-to-service-provider")
    public ResponseEntity<ApiResponse<?>> attachOrderToServiceProvider(AttachOrderRequest request) {

        String response = orderService.attachOrderToServiceProvider(request);
        return ResponseEntity.ok(success(response));
    }
}
