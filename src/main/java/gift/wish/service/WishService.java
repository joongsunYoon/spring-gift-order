package gift.wish.service;

import gift.member.entity.Member;
import gift.member.repository.MemberRepository;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.entity.Wish;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static gift.wish.dto.WishResponseDto.fromEntity;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository,
                       MemberRepository memberRepository,
                       ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Page<WishResponseDto> getWishlist(WishRequestDto dto, Pageable pageable) {

        return wishRepository.findByMemberId(dto.getMemberId() , pageable)
                .map(WishResponseDto::fromEntity);
    }

    public WishResponseDto addWish(WishRequestDto dto) {

        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(() -> new IllegalArgumentException("member를 찾을 수 없습니다"));
        Product product = productRepository.findById(dto.getProductId()).orElseThrow(() -> new IllegalArgumentException("product를 찾을 수 없습니다"));
        Optional<Wish> OpWish = wishRepository.findByMemberIdAndProductId(dto.getMemberId(),dto.getProductId());

        if(OpWish.isPresent()) {
           throw new IllegalArgumentException("이미 wishlist에 상품이 존재합니다");
        }

        Wish wish = new Wish(null, member, product, dto.getQuantity());
        return fromEntity(wishRepository.save(wish));
    }

    @Transactional
    public void deleteWish(WishRequestDto dto) {
        wishRepository.deleteByMemberIdAndProductId(dto.getMemberId(), dto.getProductId());
    }

}