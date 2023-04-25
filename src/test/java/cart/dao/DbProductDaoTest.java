package cart.dao;

import cart.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class DbProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productDao = new DbProductDao(jdbcTemplate);
    }

    @Test
    void saveTest() {
        Product gitchan = productDao.save(new Product("깃짱", "gitchan.img", 1000000000));

        List<Product> products = productDao.findAll();

        assertThat(products).contains(gitchan);
    }

    @Test
    void updateTest() {
        Product boxster = productDao.update(new Product(1L, "박스터", "boxster.img", 500));

        List<Product> products = productDao.findAll();

        assertThat(products)
                .extracting("name")
                .contains(boxster.getName());
    }

    @Test
    void deleteTest() {
        Product gitchan = productDao.save(new Product("깃짱", "gitchan.img", 1000000000));

        productDao.deleteById(gitchan.getId());

        List<Product> products = productDao.findAll();
        assertThat(products).doesNotContain(gitchan);
    }
}