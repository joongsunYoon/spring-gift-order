package gift.external.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.KakaoException;
import gift.external.kakao.dto.KakaoErrorResponse;
import gift.external.kakao.dto.KakaoInfoResponseDto;
import gift.external.kakao.dto.KakaoTokenResponseDto;
import gift.external.kakao.entity.KakaoToken;
import gift.external.kakao.repository.KakaoTokenRepository;
import gift.jwt.JwtProvider;
import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
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
import java.util.Optional;

@Service
public class KakaoTokenService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoTokenService(RestTemplateBuilder restTemplateBuilder,
                             ObjectMapper objectMapper,
                             KakaoTokenRepository kakaoTokenRepository,
                             JwtProvider jwtProvider,
                             MemberRepository memberRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
        this.kakaoTokenRepository = kakaoTokenRepository;
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    public KakaoTokenResponseDto getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

        try {
            var response = restTemplate.exchange(request, KakaoTokenResponseDto.class);
            KakaoTokenResponseDto kakaoTokenResponseDto = response.getBody();

            //값이 정상적으로 들어왔을 경우 db에 저장
            if (response.getStatusCode().is2xxSuccessful()) {

                //TODO: 사용자 정보를 가져워사 member 찾을 수 있도록 바꿔야함
                Member member = new Member(
                        getUserId(kakaoTokenResponseDto.accessToken()).id(),
                        "",
                        "1",
                        ""
                );

                String token = jwtProvider.generateToken(member);
                kakaoTokenRepository.save(new KakaoToken(
                        token,
                        kakaoTokenResponseDto.accessToken(),
                        kakaoTokenResponseDto.expiresIn(),
                        kakaoTokenResponseDto.refreshToken(),
                        kakaoTokenResponseDto.refreshTokenExpiresIn()
                ));
            }

            return response.getBody();

        } catch (HttpClientErrorException ex) {
            try {
                KakaoErrorResponse errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), KakaoErrorResponse.class);
                throw new KakaoException(errorResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String findAccessToken(Member member) {
        return kakaoTokenRepository.findByMemberId(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("Member가 부적절합니다")
        ).getAccessToken();
    }

    public KakaoInfoResponseDto getUserId(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        var request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        try {
            var response = restTemplate.exchange(request, KakaoInfoResponseDto.class);
            return response.getBody();

        } catch (HttpClientErrorException ex) {
            try {
                KakaoErrorResponse errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), KakaoErrorResponse.class);
                throw new KakaoException(errorResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
