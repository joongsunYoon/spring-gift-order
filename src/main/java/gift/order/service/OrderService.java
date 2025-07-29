package gift.order.service;

import gift.order.dto.OrderRequestDto;
import gift.order.dto.OrderResponseDto;
import gift.order.entity.Order;
import gift.order.repository.OrderRepository;
import gift.product.entity.Option;
import gift.product.repository.OptionRepository;
import gift.wish.repository.WishRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static gift.order.dto.OrderResponseDto.fromEntity;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;

    public OrderService(OrderRepository orderRepository ,
                        OptionRepository optionRepository,
                        WishRepository wishRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
    }

    public List<OrderResponseDto> findAll(Long memberId) {

        return orderRepository.findByMemberId(memberId).stream().map(OrderResponseDto::fromEntity).toList();
    }

    public OrderResponseDto findOrder(Long id) {
        return fromEntity(orderRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디 값이 부적절 합니다")
        ));
    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, Long memberId) {

        Option option = optionRepository.findById(orderRequestDto.optionId()).orElseThrow(
                () -> new IllegalArgumentException("Option id가 부적절합니다")
        );

        // 수량 차감
        option.subtractQuantity(orderRequestDto.quantity());

        //위시리스트에 있을 경우 삭제
        wishRepository.findByMemberIdAndProductId(memberId, option.getId())
                .ifPresent(wishRepository::delete);

        Order order = new Order(
                orderRequestDto.quantity(),
                orderRequestDto.message(),
                option,
                LocalDateTime.now(),
                memberId
        );

        return fromEntity(orderRepository.save(order));
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
