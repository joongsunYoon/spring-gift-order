package gift.order.dto;

public record OrderRequestDto(
        Long optionId,
        Long quantity,
        String message
) {}
