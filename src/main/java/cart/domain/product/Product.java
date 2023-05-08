package cart.domain.product;

import java.util.Objects;

public class Product {

    private static final int MAX_NAME_LENGTH = 255;

    private final ProductId productId;
    private final String productName;
    private final Image image;
    private final Price price;

    public Product(final ProductId productId, final String productName, final Image image,
            final Price price) {
        validateName(productName);
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.price = price;
    }

    public Product(final String productName, final String productImage, final int productPrice) {
        this(new ProductId(), productName, productImage, productPrice);
    }

    public Product(final Long productId, final Product other) {
        this(new ProductId(productId), other.productName, other.image, other.price);
    }

    public Product(final Long productId, final String productName, final String productImage, final int productPrice) {
        this(new ProductId(productId), productName, new Image(productImage), new Price(productPrice));
    }

    public Product(final ProductId id, final String name, final String image, final int price) {
        this(id, name, new Image(image), new Price(price));
    }

    private void validateName(final String productName) {
        if (productName == null) {
            throw new IllegalArgumentException("이름은 null일 수 없습니다.");
        }
        if (productName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("이름은 " + MAX_NAME_LENGTH + "자 이하만 가능합니다.");
        }
        if (productName.isBlank()) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }
    }

    public ProductId getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Image getProductImage() {
        return image;
    }

    public Price getProductPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Product product = (Product) o;
        return productId.equals(product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
