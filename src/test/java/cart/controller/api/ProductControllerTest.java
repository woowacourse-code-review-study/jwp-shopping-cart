package cart.controller.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import cart.controller.dto.ProductRequest;
import cart.dao.ProductDao;
import cart.domain.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("ProductController 는")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void 상품을_등록한다() {
        final ProductRequest request = new ProductRequest("chaechae", "https://www.chaechae.com", 1000);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(toJson(request))
                .when()
                .post("/products")
                .then()
                .log().all()
                .statusCode(201);
    }

    @Test
    public void 상품을_수정한다() {
        final Long id = productDao.save(new Product("mallang", "https://www.mallang.com", 2000));
        final ProductRequest request = new ProductRequest("chaechae", "https://www.chaechae.com", 1000);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(toJson(request))
                .when()
                .put("/products/" + id)
                .then()
                .log().all()
                .statusCode(200);
        final Product product = productDao.findById(id).get();
        assertThat(product.getName()).isEqualTo("chaechae");
    }

    @Test
    public void 상품을_삭제한다() {
        final Long id = productDao.save(new Product("mallang", "https://www.mallang.com", 2000));

        given()
                .when()
                .delete("/products/" + id)
                .then()
                .statusCode(200);
        assertThat(productDao.findById(id)).isEmpty();
    }

    @Test
    public void 상품_등록_시_예외_처리() {
        final ProductRequest request = new ProductRequest("  ", "chaechae.com", 0);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(toJson(request))
                .when()
                .post("/products")
                .then()
                .statusCode(400);
    }

    private String toJson(final ProductRequest request) {
        try {
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
