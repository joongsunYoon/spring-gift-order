package gift.product.controller;

import gift.exception.GlobalExceptionHandler.ApiResponse;
import gift.product.dto.*;
import gift.product.entity.Option;
import gift.product.service.OptionService;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gift.product.dto.ProductPageDto.fromEntity;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    //단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        ProductResponseDto productResponseDto = productService.getProduct(id);
        return  ResponseEntity.ok(productResponseDto);
    }

    //전체 조회
    @GetMapping
    public ResponseEntity<ProductPageDto<ProductResponseDto>> getAllProducts(@PageableDefault(size = 5, sort = "name") Pageable pageable) {
        Page<ProductResponseDto> productResponseDtos = productService.getAllProducts(pageable);
        return  ResponseEntity.ok(fromEntity(productResponseDtos));
    }

    //특정 상품 추가
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> addProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {

        productService.validateProduct(productRequestDto);
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto);
        return  ResponseEntity.ok(new ApiResponse<>(200,"추가 완료" , productResponseDto));
    }

    //특정 상품 수정
    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(@Valid @RequestBody ProductRequestDto productRequestDto,
                                                            @PathVariable Long id) {

        productService.validateProduct(productRequestDto);
        ProductResponseDto productResponseDto = productService.updateProduct(id,productRequestDto);
        return  ResponseEntity.ok(new ApiResponse<>(200,"수정 완료" , productResponseDto));
    }

    //삭제
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<ApiResponse<OptionResponseDto>> addOption(@PathVariable Long id, @Valid @RequestBody OptionRequestDto optionRequestDto) {
        OptionResponseDto optionResponseDto = optionService.addOption(id, optionRequestDto);
        return  ResponseEntity.ok(new ApiResponse<>(204,"Option 추가 성공", optionResponseDto));
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<ApiResponse<List<OptionResponseDto>>> getOption(@PathVariable Long id) {
        List<OptionResponseDto> optionResponseDto = optionService.getOption(id);
        return  ResponseEntity.ok(new ApiResponse<>(200,"Option 조회 성공", optionResponseDto));
    }

}
