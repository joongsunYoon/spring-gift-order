package gift.wish.controller;

import gift.auth.LoginMember;
import gift.exception.GlobalExceptionHandler.ApiResponse;
import gift.member.entity.Member;
import gift.wish.dto.WishPageDto;
import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/wishlists")
public class WishController {

    private final WishService wishService;
    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<WishPageDto<WishResponseDto>>> getWishlist(@LoginMember Member member ,
                                                                                 @PageableDefault(size = 5, sort = "quantity", direction = Sort.Direction.DESC) Pageable pageable) {
        WishRequestDto wishRequestDto = new WishRequestDto();
        wishRequestDto.setMemberId(member.getId());
        return ResponseEntity.ok(new ApiResponse<>(200,"조회에 성공했습니다", WishPageDto.from(wishService.getWishlist(wishRequestDto,pageable))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WishResponseDto>> addWish(@RequestBody WishRequestDto dto,
                                                                @LoginMember Member member) {

        dto.setMemberId(member.getId());
        return ResponseEntity.ok(new ApiResponse<>(200,"추가에 성공했습니다", wishService.addWish(dto)));
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteWish(@PathVariable Long productId,
                                                        @LoginMember Member member) {
        WishRequestDto dto = new WishRequestDto();
        dto.setMemberId(member.getId());
        dto.setProductId(productId);
        wishService.deleteWish(dto);
        return ResponseEntity.ok(new ApiResponse<>(200,"삭제에 성공했습니다", null));
    }

}
