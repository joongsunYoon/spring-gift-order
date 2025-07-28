package gift.order.service;

import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderResponseDto> findAll(Long memberId) {

    }

    public OrderResponseDto findOrder(Long id) {

    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {

    }

    public OrderResponseDto updateOrder(Long id , OrderRequestDto orderRequestDto) {

    }

    public void deleteOrder(Long id) {

    }

}
