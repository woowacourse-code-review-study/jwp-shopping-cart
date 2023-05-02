package cart.service;

import cart.domain.Product;
import cart.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductRepository productRepository;

    public ProductQueryService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> find() {
        return productRepository.find();
    }
}
