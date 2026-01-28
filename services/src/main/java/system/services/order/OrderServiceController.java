package system.services.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.services.config.ApiResponse;
import system.services.config.BaseController;
import system.services.order.dto.AttachOrderRequest;
import system.services.order.dto.OrderSearchRequest;
import system.services.order.dto.RequestServiceRequest;
import system.services.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @GetMapping("/search-orders")
    public ResponseEntity<ApiResponse<?>> searchOrders(
            @RequestParam(required = false) String serviceId,
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) BigDecimal minBudget,
            @RequestParam(required = false) BigDecimal maxBudget,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            Pageable pageable
    ) {

        OrderSearchRequest request = new OrderSearchRequest(serviceId, serviceName,
                status, minBudget, maxBudget, fromDate, toDate
        );

        var responses = orderService.searchOrderService(request, pageable);
        return ResponseEntity.ok(success(responses));
    }
}
