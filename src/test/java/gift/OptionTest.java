package gift;

import gift.exception.GlobalExceptionHandler;
import gift.member.dto.MemberRequestDto;
import gift.member.dto.MemberResponseDto;
import gift.member.entity.Member;
import gift.product.dto.OptionResponseDto;
import gift.product.service.OptionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OptionTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;
    private String baseUrl;
    private String token;

    @BeforeEach
    void setUp() {

        String email = "test";
        String password = "1234";
        String role = "admin";
        Member member = new Member(null , email, password, role);
        MemberRequestDto memberRequestDto = MemberRequestDto.fromEntity(member);
        RestClient firstClient = RestClient.builder().baseUrl("http://localhost:" + port).build();

        var response = firstClient.post()
                .uri("http://localhost:" + port + "/api/members/register")
                .body(memberRequestDto)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<GlobalExceptionHandler.ApiResponse<MemberResponseDto>>() {});

        token = Objects.requireNonNull(response.getBody()).data().token();

        baseUrl = "http://localhost:" + port + "/api/products";
        restClient = RestClient.builder().baseUrl(baseUrl)
                .defaultHeader("Authorization", token)
                .build();
    }

}
