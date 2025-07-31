package gift.order.dto;

import gift.order.entity.Order;

import java.time.LocalDateTime;

public record OrderResponseDto(
        Long id,
        Long optionId,
        Long quantity,
        String message,
        LocalDateTime orderDateTime
){

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
