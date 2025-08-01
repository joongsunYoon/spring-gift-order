package gift.product.service;

import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static gift.product.dto.ProductResponseDto.fromEntity;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //단건 조회
    public ProductResponseDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("id가 옳바르지 않습니다."));
        return fromEntity(product);
    }

    //전체 조회
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(ProductResponseDto::fromEntity);
    }

    //추가
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();

        if(productRequestDto != null) {
            product.setName(productRequestDto.getName());
            product.setPrice(productRequestDto.getPrice());
            product.setImageUrl(productRequestDto.getImageUrl());
            return fromEntity(productRepository.save(product));
        }
        return null;
    }

    //수정
    public ProductResponseDto updateProduct(Long id ,ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("id가 옳바르지 않습니다."));
        if(productRequestDto != null) {
            product.setName(productRequestDto.getName());
            product.setPrice(productRequestDto.getPrice());
            product.setImageUrl(productRequestDto.getImageUrl());

            return fromEntity(productRepository.save(product));
        }
        return null;
    }

    //삭제
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    //상품 이름 검사
    public void validateProduct(ProductRequestDto productRequestDto) {
        if(productRequestDto.getName().contains("카카오")){
            throw new IllegalArgumentException("카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.");
        }
    }
}
