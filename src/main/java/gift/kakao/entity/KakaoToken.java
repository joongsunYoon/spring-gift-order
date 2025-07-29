package gift.kakao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class KakaoToken{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private Integer refreshTokenExpiresIn;
    private Long memberId;

    public KakaoToken() {}
    public KakaoToken(String accessToken, Integer expiresIn, String refreshToken, Integer refreshTokenExpiresIn, Long memberId) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.memberId = memberId;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
