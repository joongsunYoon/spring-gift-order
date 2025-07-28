package gift.kakao.dto;

public record KakaoTokenResponseDto(
        String tokenType,
        String accessToken,
        int expiresIn,
        String refreshToken,
        int refreshTokenExpiresIn
) {}
