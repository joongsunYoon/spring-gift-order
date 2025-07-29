package gift.external.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.KakaoException;
import gift.external.kakao.dto.KakaoErrorResponse;
import gift.external.kakao.dto.KakaoMessageRequestDto;
import gift.external.kakao.dto.KakaoMessageResponseDto;
import gift.order.dto.OrderRequestDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static gift.external.kakao.dto.KakaoMessageRequestDto.fromOrderRequestDto;

@Service
public class KakaoMessageService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoMessageService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public KakaoMessageResponseDto sendMessage(OrderRequestDto orderRequestDto, String accessToken){
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(accessToken);

        KakaoMessageRequestDto kakaoMessageRequestDto = fromOrderRequestDto(orderRequestDto);

        try{
            String json = objectMapper.writeValueAsString(kakaoMessageRequestDto.templateObject());
            LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("template_object" , json);

            var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
            var response = restTemplate.exchange(request, KakaoMessageResponseDto.class);

            return response.getBody();

        } catch (HttpClientErrorException ex) {
            try{
                KakaoErrorResponse errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), KakaoErrorResponse.class);
                throw new KakaoException(errorResponse);
            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
