package gift.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.KakaoException;
import gift.kakao.dto.KakaoErrorResponse;
import gift.kakao.dto.KakaoMessageRequestDto;
import gift.kakao.dto.KakaoMessageResponseDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.entity.KakaoToken;
import gift.kakao.repository.KakaoRepository;
import gift.member.entity.Member;
import gift.order.dto.OrderRequestDto;
import org.springframework.beans.factory.annotation.Value;
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

import static gift.kakao.dto.KakaoMessageRequestDto.fromOrderRequestDto;

@Service
public class KakaoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final KakaoRepository kakaoRepository;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper, KakaoRepository kakaoRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.kakaoRepository = kakaoRepository;
    }

    public KakaoTokenResponseDto getAccessToken(String code , Long memberId) {
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
            KakaoTokenResponseDto kakaoTokenResponseDto = response.getBody();

            //값이 정상적으로 들어왔을 경우 db에 저장
            if(response.getStatusCode().is2xxSuccessful()) {
                kakaoRepository.save(new KakaoToken(
                        kakaoTokenResponseDto.accessToken(),
                        kakaoTokenResponseDto.expiresIn(),
                        kakaoTokenResponseDto.refreshToken(),
                        kakaoTokenResponseDto.refreshTokenExpiresIn(),
                        memberId
                ));
            }

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

    public String findAccessToken(Member member) {
        return kakaoRepository.findByMemberId(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("Member가 부적절합니다")
        ).getAccessToken();
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
        } catch(JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
