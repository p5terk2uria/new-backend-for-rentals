package system.services.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.services.config.ApiResponse;
import system.services.config.BaseController;
import system.services.order.dto.*;
import system.services.order.enums.OrderPaymentStatus;
import system.services.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/services/order")
public class OrderServiceController extends BaseController {

    private final OrderService orderService;

    @PostMapping("/service")
    public ResponseEntity<ApiResponse<?>> requestService(@RequestBody RequestServiceRequest request) {

        return ResponseEntity.ok(success( orderService.requestService(request)));
    }

    @PostMapping("/attach-order-to-service-provider")
    public ResponseEntity<ApiResponse<?>> attachOrderToServiceProvider(@RequestBody AttachOrderRequest request) {

        String response = orderService.attachOrderToServiceProvider(request);
        return ResponseEntity.ok(success(response));
    }

    @GetMapping("/search-orders")
    public ResponseEntity<ApiResponse<?>> searchOrders(
            @RequestParam (required = false) String userId,
            @RequestParam(required = false) String serviceId,
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) BigDecimal minBudget,
            @RequestParam(required = false) BigDecimal maxBudget,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            Pageable pageable
    ) {

        OrderSearchRequest request = new OrderSearchRequest(userId, serviceId, serviceName,
                status, minBudget, maxBudget, fromDate, toDate
        );

        var responses = orderService.searchOrderService(request, pageable);
        return ResponseEntity.ok(success(responses));
    }

    @GetMapping("/find-by-order-trackingId")
    public ResponseEntity<AdminOrderResponse> getOrderByOrderTrackingId(
            @RequestParam("orderTrackingId") String orderTrackingId) {
        var response = orderService.getServiceOrderByOrderTrackingId(orderTrackingId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-order-payment-status")
    public void updateOrderPaymentStatus(@RequestParam String orderId, @RequestParam OrderPaymentStatus paymentStatus) {
        orderService.updateOrderPaymentStatus(orderId, paymentStatus);
    }

    @GetMapping("/find-all-orders")
    public ResponseEntity<ApiResponse<?>> getAllOrders(Pageable pageable) {

        var orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(success(orders));
    }

    @PutMapping("/update-order-status")
    public ResponseEntity<ApiResponse<?>> updateOrderStatus(@RequestParam String orderId, @RequestParam OrderStatus orderStatus) {

        orderService.updateOrderStatus(orderId, orderStatus);
        return ResponseEntity.ok(success("Status updated successfully"));
    }
}
