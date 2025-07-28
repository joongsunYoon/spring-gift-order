package gift.order.dto;

import java.time.LocalDateTime;

public record OrderResponseDto(
        Long id,
        Long optionId,
        Long quantity,
        String message,
        LocalDateTime orderDateTime
) {}
