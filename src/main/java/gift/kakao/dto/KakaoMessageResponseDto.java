package gift.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMessageResponseDto(@JsonProperty("result_code") Integer resultCode) {}
