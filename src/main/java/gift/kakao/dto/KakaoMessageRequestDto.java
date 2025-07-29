package gift.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.order.dto.OrderRequestDto;

public record KakaoMessageRequestDto(@JsonProperty("template_object") Text templateObject) {

    public record Text(
            @JsonProperty("object_type") String objectType,
            String text,
            Link link
    ){}

    public record Link(
            @JsonProperty("web_url") String webUrl,
            @JsonProperty("mobile_web_url") String mobileWebUrl
    ){}

    public static KakaoMessageRequestDto fromOrderRequestDto(OrderRequestDto dto) {

        Link link = new Link("http://localhost:8080","http://localhost:8080");
        Text templateObject = new Text("text", dto.message(), link);

        return new KakaoMessageRequestDto(templateObject);
    }
}
