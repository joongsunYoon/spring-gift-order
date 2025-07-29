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

        return orderRepository.findByMemberId(memberId).stream().map(OrderResponseDto::fromEntity).toList();
    }

    public OrderResponseDto findOrder(Long id) {
        return fromEntity(orderRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디 값이 부적절 합니다")
        ));
    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {

    }

    public OrderResponseDto updateOrder(Long id , OrderRequestDto orderRequestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디 값이 부적절 합니다")
        );

        Option option = optionRepository.findById(orderRequestDto.optionId()).orElseThrow(
                () -> new IllegalArgumentException("Option id 값이 부적절합니다")
        );

        Order savedOrder = new Order(
                orderRequestDto.quantity(),
                orderRequestDto.message(),
                option,
                order.getOrderDateTime(),
                order.getMemberId()

        );

        return fromEntity(orderRepository.save(savedOrder));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}
