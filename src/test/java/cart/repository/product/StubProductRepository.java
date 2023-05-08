package cart.repository.product;

import cart.domain.product.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class StubProductRepository implements ProductRepository {

    private final Map<Long, Product> productMap = new HashMap<>();
    private final AtomicLong maxId = new AtomicLong();

    @Override
    public Product save(final Product product) {
        final long currentId = maxId.incrementAndGet();

        final Product saved = new Product(currentId, product);
        productMap.put(currentId, saved);
        return saved;
    }

    @Override
    public Product update(final Product product) {
        final Long id = product.getProductId().getValue();
        if (id == null || productMap.get(id) == null) {
            return product;
        }
        productMap.put(id, product);
        return product;
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(productMap.values());
    }

    @Override
    public Optional<Product> findById(final Long id) {
        final Product product = productMap.get(id);
        if (product == null) {
            return Optional.empty();
        }
        return Optional.of(product);
    }

    @Override
    public void deleteById(final Long id) {
        productMap.remove(id);
    }

    @Override
    public List<Product> findAllById(final List<Long> ids) {
        final List<Product> products = new ArrayList<>();
        for (final Long id : ids) {
            final Product product = productMap.get(id);
            if (product != null) {
                products.add(product);
            }
        }
        return products;
    }
}
