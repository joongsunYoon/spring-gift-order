package gift.product.dto;

import gift.product.entity.Option;

public class OptionResponseDto {

    private final Long id;
    private final String name;
    private final Long quantity;

    private OptionResponseDto(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public static OptionResponseDto fromEntity(Option option) {
        return new OptionResponseDto(
                option.getId(),
                option.getName(),
                option.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }
}
