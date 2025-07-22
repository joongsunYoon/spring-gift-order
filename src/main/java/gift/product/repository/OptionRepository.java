package gift.product.repository;

import gift.product.entity.Option;
import gift.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByProduct(Product product);
    Optional<Option> findByName(String name);
}
