package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.product.ProductEntity;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class ProductDaoTest {

    @Autowired
    private DataSource dataSource;

    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new ProductDao(dataSource);
    }

    @Test
    @DisplayName("상품을 DB에 저장한다.")
    void save() {
        //given
        final ProductEntity productEntity = new ProductEntity(
            null,
            "다즐",
            "스플릿.com",
            10000000,
            "다즐짱"
        );

        //when
        final Long savedProductId = productDao.save(productEntity);

        //then
        assertThat(savedProductId).isNotNull();
    }

    @Test
    @DisplayName("모든 상품을 조회한다.")
    void findAll() {
        //given
        final ProductEntity firstProductEntity = new ProductEntity(
            null,
            "다즐",
            "스플릿.com",
            10000000,
            "다즐짱"
        );
        final ProductEntity secondProductEntity = new ProductEntity(
            null,
            "다즐",
            "스플릿.com",
            10000000,
            "다즐짱"
        );

        //when
        final Long savedFirstProductId = productDao.save(firstProductEntity);
        final Long savedSecondProductId = productDao.save(secondProductEntity);

        //then
        assertThat(productDao.findAll()).hasSize(2);
        assertThat(productDao.findAll()).map(ProductEntity::getId)
            .containsExactly(savedFirstProductId, savedSecondProductId);
    }

    @Test
    @DisplayName("ID로 상품을 조회한다.")
    void findById() {
        //given
        final ProductEntity firstProductEntity = new ProductEntity(
            null,
            "다즐",
            "스플릿.com",
            10000000,
            "다즐짱"
        );
        final Long savedFirstProductId = productDao.save(firstProductEntity);

        //when
        final ProductEntity findProductEntity = productDao.findById(savedFirstProductId);

        //then
        assertAll(
            () -> assertThat(findProductEntity.getId()).isEqualTo(savedFirstProductId),
            () -> assertThat(findProductEntity.getName()).isEqualTo("다즐"),
            () -> assertThat(findProductEntity.getImageUrl()).isEqualTo("스플릿.com"),
            () -> assertThat(findProductEntity.getPrice()).isEqualTo(10000000),
            () -> assertThat(findProductEntity.getDescription()).isEqualTo("다즐짱")
        );
    }


    @Nested
    class Delete {

        @Test
        @DisplayName("삭제하고자 하는 ID가 null이면 오류를 던진다.")
        void deleteWithNullID() {
            //given
            //when
            //then
            assertThatThrownBy(() -> productDao.delete(null))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("상품을 DB에서 삭제한다.")
        void delete() {
            //given
            final ProductEntity productEntity = new ProductEntity(
                null,
                "다즐",
                "스플릿.com",
                10000000,
                "다즐짱"
            );
            final Long savedProductId = productDao.save(productEntity);

            //when
            productDao.delete(savedProductId);

            //then
            assertThat(productDao.findAll()).hasSize(0);
        }
    }

    @Nested
    class Update {

        @Test
        @DisplayName("수정하고자 하는 데이터의 ID가 null이면 오류를 던진다.")
        void updateWithNullID() {
            //given
            final ProductEntity updateProductEntity = new ProductEntity(null, "name", "imageUrl", 1000, "description");
            //when
            //then
            assertThatThrownBy(() -> productDao.update(updateProductEntity))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("상품을 수정한다.")
        void update() {
            //given
            final ProductEntity productEntity = new ProductEntity(
                null,
                "다즐",
                "스플릿.com",
                10000000,
                "다즐짱"
            );
            final Long savedProductId = productDao.save(productEntity);
            final ProductEntity findProductEntity = productDao.findById(savedProductId);
            findProductEntity.update("name", "imageUrl", 1000, "description");

            //when
            productDao.update(findProductEntity);
            final ProductEntity updatedProductEntity = productDao.findById(findProductEntity.getId());

            //then
            assertAll(
                () -> assertThat(updatedProductEntity.getName()).isEqualTo("name"),
                () -> assertThat(updatedProductEntity.getImageUrl()).isEqualTo("imageUrl"),
                () -> assertThat(updatedProductEntity.getPrice()).isEqualTo(1000),
                () -> assertThat(updatedProductEntity.getDescription()).isEqualTo("description")
            );
        }
    }
}
