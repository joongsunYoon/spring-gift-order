package gift.order.dto;

import gift.order.entity.Order;

import java.time.LocalDateTime;

public class OrderResponseDto{

    private final Long id;
    private final Long optionId;
    private final Long quantity;
    private final String message;
    private final LocalDateTime orderDateTime;

    private OrderResponseDto(Long id, Long optionId, Long quantity, String message, LocalDateTime orderDateTime) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.orderDateTime = orderDateTime;
    };

    public static OrderResponseDto fromEntity(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getOption().getId(),
                order.getQuantity(),
                order.getMessage(),
                order.getOrderDateTime()
        );
    }

}
