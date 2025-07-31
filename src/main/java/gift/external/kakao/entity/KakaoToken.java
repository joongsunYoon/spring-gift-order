package gift.external.kakao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class KakaoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private Integer refreshTokenExpiresIn;
    private Long memberId;

    protected KakaoToken() {}

    public KakaoToken(String token, String accessToken, Integer expiresIn, String refreshToken, Integer refreshTokenExpiresIn) {
        this.token = token;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public String getToken() {
        return token;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
