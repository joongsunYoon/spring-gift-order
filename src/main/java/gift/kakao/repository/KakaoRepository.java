package gift.kakao.repository;

import gift.kakao.entity.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoRepository extends JpaRepository<KakaoToken,Long> {
    Optional<KakaoToken> findByMemberId(Long memberId);
}
