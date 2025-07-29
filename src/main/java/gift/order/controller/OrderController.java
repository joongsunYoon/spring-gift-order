package gift.order.controller;

import gift.auth.LoginMember;
import gift.exception.GlobalExceptionHandler.ApiResponse;
import gift.kakao.service.KakaoService;
import gift.member.entity.Member;
import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final KakaoService kakaoService;
    public OrderController(OrderService orderService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                                                     @LoginMember Member member) {
        OrderResponseDto dto = orderService.createOrder(orderRequestDto,member.getId());
        String kakaoToken = kakaoService.findAccessToken(member);
        kakaoService.sendMessage(orderRequestDto, kakaoToken);
        return ResponseEntity.status(201).body(new ApiResponse<>(201,"주문 성공", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrder(@PathVariable Long id) {
        OrderResponseDto dto = orderService.findOrder(id);
        return ResponseEntity.ok(new ApiResponse<>(200,"주문 단일 조회 성공", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getAllOrders(@LoginMember Member member) {
        List<OrderResponseDto> dto = orderService.findAll(member.getId());
        return ResponseEntity.ok(new ApiResponse<>(200,"주문 전체 조회 성공", dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateOrder(@PathVariable Long id,
                                                        @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto dto = orderService.updateOrder(id, orderRequestDto);
        return ResponseEntity.status(200).body(new ApiResponse<>(200, "주문 수정 성공" , dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
