package gift.wish.dto;

import gift.wish.entity.Wish;

public class WishRequestDto {

    private Long id;
    private Long memberId;
    private Long productId;
    private Integer quantity;

    public WishRequestDto() {}
    private WishRequestDto(Long id ,Long memberId, Long productId, Integer quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static WishRequestDto fromEntity(Wish wish) {
        return new WishRequestDto(
                wish.getId(),
                wish.getMember().getId(),
                wish.getProduct().getId(),
                wish.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
