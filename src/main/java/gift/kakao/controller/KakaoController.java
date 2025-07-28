package gift.kakao.controller;

import gift.exception.GlobalExceptionHandler.ApiResponse;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<KakaoTokenResponseDto>> callback(@RequestParam("code") String code) {
        KakaoTokenResponseDto token = kakaoService.getAccessToken(code);
        return ResponseEntity.ok(new ApiResponse<>(200,"토큰 발급 성공" , token));
    }

}
