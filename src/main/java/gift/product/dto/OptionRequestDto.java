package gift.product.dto;

import gift.product.entity.Option;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionRequestDto {

    private final Long id;
    //최소 한 글자, 최대 50자
    @Size(min = 1, max = 50, message = "최소 한글자, 최대 50자를 입력할 수 있습니다.")

    //특수문자 제한
    @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*$", message = "특수문자는 (), [], +, -, &, /, _ 만 허용됩니다.")
    private final String name;

    //1<= x < 100_000_000
    @Min(1)
    @Max(100_000_000 - 1)
    private final Long quantity;

    private OptionRequestDto(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public static OptionRequestDto fromEntity(Option option) {
        return new OptionRequestDto(
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
