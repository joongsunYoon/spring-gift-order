package gift.product.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import gift.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    
}
