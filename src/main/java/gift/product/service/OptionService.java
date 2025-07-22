package gift.product.service;

import gift.product.dto.OptionRequestDto;
import gift.product.dto.OptionResponseDto;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static gift.product.dto.OptionResponseDto.fromEntity;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    //옵션 추가
    public OptionResponseDto addOption(Long id , OptionRequestDto dto){

        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Product id가 옳지 않습니다.")
        );

        //옵션 중복 불가
        if(optionRepository.findByName(dto.getName()).isPresent()){
            throw new IllegalArgumentException("Option 이름이 이미 존재합니다.");
        }

        Option option = new Option(
                null,
                dto.getName(),
                dto.getQuantity(),
                product
        );

        return fromEntity(optionRepository.save(option));
    }

    //옵션 조회
    public List<OptionResponseDto> getOption(Long id){

        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Product id가 옳지 않습니다.")
        );

        return optionRepository.findByProduct(product)
                .stream().map(OptionResponseDto::fromEntity).toList();
    }

}
