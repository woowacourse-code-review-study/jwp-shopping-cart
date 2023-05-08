package cart.domain.product;

import java.util.Objects;

public class ProductId {

    private final Long value;

    public ProductId(final Long value) {
        this.value = value;
    }

    public ProductId() {
        this(null);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductId productId = (ProductId) o;
        return Objects.equals(value, productId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
