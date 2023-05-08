package cart.dao;

import cart.entity.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
public class H2ProductDao implements ProductDao {

    private static final RowMapper<Product> productRowMapper = (resultSet, rowMapper) -> new Product(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("img_url"),
            resultSet.getInt("price"),
            resultSet.getBoolean("is_delete")
    );

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public H2ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public Product save(Product product) {
        String sql = "INSERT INTO product (name, img_url, price) VALUES (:name, :imgUrl, :price)";

        SqlParameterSource parameters = new BeanPropertySqlParameterSource(product);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, parameters, keyHolder);

        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Product(id, product.getName(), product.getImgUrl(), product.getPrice(), false);
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM product WHERE is_delete = false";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT * FROM product WHERE id = :id";
        Map<String, Long> parameter = Collections.singletonMap("id", id);
        return findProduct(sql, parameter);
    }

    private Optional<Product> findProduct(String sql, Map<String, Long> parameter) {
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, parameter, productRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE product SET name = :name, img_url = :imgUrl, price = :price WHERE id = :id";
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(product);
        namedParameterJdbcTemplate.update(sql, parameters);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "UPDATE product SET is_delete = true WHERE id = :id";
        Map<String, Long> parameter = Collections.singletonMap("id", id);
        namedParameterJdbcTemplate.update(sql, parameter);
    }

    @Override
    public List<Product> findByIds(List<Long> ids) {
        String sql = "SELECT * FROM product WHERE id = :id";
        List<Product> products = new ArrayList<>();
        for (Long productId : ids) {
            Map<String, Long> parameter = Collections.singletonMap("id", productId);
            Product product = namedParameterJdbcTemplate.queryForObject(sql, parameter, productRowMapper);
            products.add(product);
        }
        return products;
    }
}
