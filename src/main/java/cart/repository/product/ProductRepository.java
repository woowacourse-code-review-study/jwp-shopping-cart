package cart.repository.product;

import cart.domain.product.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Product update(Product product);

    List<Product> findAll();

    Optional<Product> findById(Long id);

    void deleteById(Long id);

    List<Product> findAllById(List<Long> ids);
}
