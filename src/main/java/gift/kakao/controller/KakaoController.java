package gift.kakao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.auth.LoginMember;
import gift.exception.GlobalExceptionHandler.ApiResponse;
import gift.kakao.dto.KakaoMessageResponseDto;
import gift.kakao.dto.KakaoTokenResponseDto;
import gift.kakao.service.KakaoService;
import gift.member.entity.Member;
import gift.order.dto.OrderRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoController {

    private final KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @GetMapping("/callback")
    public ResponseEntity<ApiResponse<KakaoTokenResponseDto>> callback(@RequestParam("code") String code,
                                                                       @LoginMember Member member) {
        KakaoTokenResponseDto token = kakaoService.getAccessToken(code,member.getId());
        return ResponseEntity.ok(new ApiResponse<>(200,"토큰 발급 성공" , token));
    }

}
