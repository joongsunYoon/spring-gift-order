package gift;

import gift.auth.LoginMemberArgumentResolver;
import gift.exception.KakaoException;
import gift.interceptor.Interceptor;
import gift.interceptor.WebMvcConfig;
import gift.external.kakao.controller.KakaoController;
import gift.external.kakao.dto.KakaoErrorResponse;
import gift.external.kakao.dto.KakaoTokenResponseDto;
import gift.external.kakao.service.KakaoTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = KakaoController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        WebMvcConfig.class,
                        LoginMemberArgumentResolver.class,
                        Interceptor.class
                }
        )
)
public class KakaoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KakaoTokenService kakaoTokenService;

    @Test
    void callback_정상동작_테스트() throws Exception {
        //given
        String code = "abc123";
        KakaoTokenResponseDto mockToken = new KakaoTokenResponseDto(
                "bearer",
                "access-token",
                1000,
                "refresh-token",
                10000
        );

        when(kakaoTokenService.getAccessToken(code)).thenReturn(mockToken);

        //when & then
        mockMvc.perform(get("/oauth/kakao/callback")
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("토큰 발급 성공"))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.tokenType").value("bearer"));
    }

    @Test
    void callback_비정상동작_테스트() throws Exception {

        //given
        String code = "invalid";
        KakaoErrorResponse kakaoError = new KakaoErrorResponse(
                "invalid_grant",
                "authorization code not found for code",
                "KOE320"
        );

        when(kakaoTokenService.getAccessToken(code))
                .thenThrow(new KakaoException(kakaoError));

        //when & then
        mockMvc.perform(get("/oauth/kakao/callback")
                        .param("code", code))
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message").value("카카오 인증 에러"))
                .andExpect(jsonPath("$.data.error").value("invalid_grant"))
                .andExpect(jsonPath("$.data.error_description")
                        .value("authorization code not found for code"))
                .andExpect(jsonPath("$.data.error_code").value("KOE320"));
    }
}
