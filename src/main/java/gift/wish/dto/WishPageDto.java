package gift.wish.dto;

import gift.product.dto.ProductPageDto;
import org.springframework.data.domain.Page;

import java.util.List;

public class WishPageDto<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean last;

    private WishPageDto(
            List<T> content,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean last
    ) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;

    }

    public static <T> WishPageDto<T> from(Page<T> page) {
        return new WishPageDto<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
