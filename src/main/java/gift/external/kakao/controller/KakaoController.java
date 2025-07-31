package gift.external.kakao.controller;

import gift.auth.LoginMember;
import gift.exception.GlobalExceptionHandler.ApiResponse;
import gift.external.kakao.dto.KakaoTokenResponseDto;
import gift.external.kakao.service.KakaoTokenService;
import gift.member.entity.Member;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoController {

    private final KakaoTokenService kakaoTokenService;

    public KakaoController(KakaoTokenService kakaoTokenService) {
        this.kakaoTokenService = kakaoTokenService;
    }

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @GetMapping("/authorize")
    public void authorize(HttpServletResponse response) throws IOException {
        String redirectUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";

        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<KakaoTokenResponseDto>> callback(@RequestParam("code") String code) {
        KakaoTokenResponseDto token = kakaoTokenService.getAccessToken(code);
        return ResponseEntity.ok(new ApiResponse<>(200, "토큰 발급 성공", token));
    }

}
