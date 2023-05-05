package cart.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CartEntity {
    private final Long id;
    private final Long memberId;
    private final Long productId;
    private int count;
    private final Timestamp createdAt;
    private Timestamp updatedAt;

    public CartEntity(Long id, Long memberId, Long productId, int count, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.count = count;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getCount() {
        return count;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void replace(int count) {
        this.count = count;
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }
}
