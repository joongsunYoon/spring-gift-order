package gift.wish.repository;

import gift.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish,Long> {

    Page<Wish> findByMemberId(Long memberId , Pageable pageable);
    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);
    void deleteByMemberIdAndProductId(Long memberId, Long productId);

}
