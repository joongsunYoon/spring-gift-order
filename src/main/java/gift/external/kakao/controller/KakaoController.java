package gift.external.kakao.controller;

import gift.auth.LoginMember;
import gift.exception.GlobalExceptionHandler.ApiResponse;
import gift.external.kakao.dto.KakaoTokenResponseDto;
import gift.external.kakao.service.KakaoTokenService;
import gift.member.entity.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth/kakao")
public class KakaoController {

    private final KakaoTokenService kakaoTokenService;

    public KakaoController(KakaoTokenService kakaoTokenService) {
        this.kakaoTokenService = kakaoTokenService;
    }

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<KakaoTokenResponseDto>> callback(@RequestParam("code") String code,
                                                                       @LoginMember Member member) {
        KakaoTokenResponseDto token = kakaoTokenService.getAccessToken(code,member.getId());
        return ResponseEntity.ok(new ApiResponse<>(200,"토큰 발급 성공" , token));
    }

}
