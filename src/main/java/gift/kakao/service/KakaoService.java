package gift.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.KakaoException;
import gift.kakao.dto.KakaoErrorResponse;
import gift.kakao.dto.KakaoTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoService {

    private final RestTemplate restTemplate;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper, KakaoRepository kakaoRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public KakaoTokenResponseDto getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        try{
            var response = restTemplate.exchange(request, KakaoTokenResponseDto.class);
            return response.getBody();

        } catch (HttpClientErrorException ex) {
            try{
                ObjectMapper mapper = new ObjectMapper();
                KakaoErrorResponse errorResponse = mapper.readValue(ex.getResponseBodyAsString(), KakaoErrorResponse.class);
                KakaoErrorResponse errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), KakaoErrorResponse.class);
                throw new KakaoException(errorResponse);
            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

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
            System.out.println(request);
            var response = restTemplate.exchange(request, KakaoMessageResponseDto.class);

            return response.getBody();


        } catch (HttpClientErrorException ex) {
            try{
                KakaoErrorResponse errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), KakaoErrorResponse.class);
                throw new KakaoException(errorResponse);
            }catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
