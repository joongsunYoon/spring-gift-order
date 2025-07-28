package gift.order.controller;

import gift.exception.GlobalExceptionHandler.ApiResponse;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto dto = orderService.createOrder(orderRequestDto);
        return ResponseEntity.status(201).body(new ApiResponse<>(201,"주문 성공", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrder(@PathVariable Long id) {
        OrderResponseDto dto = orderService.findOrder(id);
        return ResponseEntity.ok(new ApiResponse<>(200,"주문 단일 조회 성공", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getAllOrders(@RequestParam Long memberId) {
        List<OrderResponseDto> dto = orderService.findAll(memberId);
        return ResponseEntity.ok(new ApiResponse<>(200,"주문 전체 조회 성공", dto));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrder(@PathVariable Long id,
                                                        @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto dto = orderService.updateOrder(id, orderRequestDto);
        return ResponseEntity.status(200).body(new ApiResponse<>(200, "주문 수정 성공" , dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
