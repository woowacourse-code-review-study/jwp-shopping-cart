package cart.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import cart.service.CartService;
import cart.service.ProductService;
import cart.test.ProductRequestFixture;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import java.util.Base64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@Sql("/init.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CartApiControllerTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("장바구니 목록 조회 시")
    class FindCartItemsForMemberId {

        @Test
        @DisplayName("멤버 정보가 유효하다면 장바구니 목록을 조회한다.")
        void findCartItemsForMember() {
            final Long savedProductId = productService.registerProduct(ProductRequestFixture.request);
            final Long savedCartId = cartService.putInCart(savedProductId, 1L);
            final String encodedAuth = new String(Base64.getEncoder().encode("a@a.com:password1".getBytes()));

            cartFindApi(encodedAuth)
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(1))
                    .body("[0].id", equalTo(savedCartId.intValue()))
                    .body("[0].productId", equalTo(savedProductId.intValue()));
        }

        @Test
        @DisplayName("유효하지 않은 멤버 인증이라면 401 상태를 반환한다.")
        void findCartItemsForInvalidMember() {
            final String encodedAuth = new String(Base64.getEncoder().encode("a@a.com".getBytes()));

            cartFindApi(encodedAuth)
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .body("message", equalTo("유효하지 않은 인증 정보입니다."));
        }

        private ValidatableResponse cartFindApi(final String auth) {
            return RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Basic " + auth)
                    .when()
                    .get("/carts")
                    .then();
        }
    }

    @Nested
    @DisplayName("장바구니에 상품을 담을 시")
    class PutInCart {

        @Test
        @DisplayName("멤버, 상품이 유효하다면 장바구니에 추가한다.")
        void putInCart() {
            final String encodedAuth = new String(Base64.getEncoder().encode("a@a.com:password1".getBytes()));
            final Long savedProductId = productService.registerProduct(ProductRequestFixture.request);

            cartPutApi(encodedAuth, String.valueOf(savedProductId))
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", containsString("/carts/"));
        }

        @Test
        @DisplayName("유효하지 않은 멤버 인증이라면 401 상태를 반환한다.")
        void putInCartWithInvalidAuth() {
            final String encodedAuth = new String(Base64.getEncoder().encode("a@a.com".getBytes()));
            final Long savedProductId = productService.registerProduct(ProductRequestFixture.request);

            cartPutApi(encodedAuth, String.valueOf(savedProductId))
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .body("message", equalTo("유효하지 않은 인증 정보입니다."));
        }

        @Test
        @DisplayName("상품 ID 파라미터명이 일치하지 않으면 400 상태를 반환한다.")
        void putInCartWithInvalidParameterName() {
            final String encodedAuth = new String(Base64.getEncoder().encode("a@a.com:password".getBytes()));
            final Long savedProductId = productService.registerProduct(ProductRequestFixture.request);

            cartPutApi(encodedAuth, String.valueOf(savedProductId))
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("상품 ID로 변환할 수 없는 타입이라면 400 상태를 반환한다.")
        void putInCartWithInvalidParameterType() {
            final String encodedAuth = new String(Base64.getEncoder().encode("a@a.com:password".getBytes()));

            cartPutApi(encodedAuth, "hello")
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        private ValidatableResponse cartPutApi(final String auth, final String productId) {
            return RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Basic " + auth)
                    .queryParam("productId", productId)
                    .when()
                    .post("/carts")
                    .then();
        }
    }

    @Nested
    @DisplayName("장바구니 상품 삭제 시")
    class RemoveCartItem {

        @Test
        @DisplayName("ID, 멤버가 유효하다면 장바구니 상품을 삭제한다.")
        void removeCartItem() {
            final Long savedProductId = productService.registerProduct(ProductRequestFixture.request);
            final Long savedCartId = cartService.putInCart(savedProductId, 1L);
            final String encodedAuth = new String(Base64.getEncoder().encode("a@a.com:password1".getBytes()));

            cartRemoveApi(encodedAuth, String.valueOf(savedCartId))
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("유효하지 않은 멤버 인증이라면 401 상태를 반환한다.")
        void removeCartItemWithInvalidMember() {
            final Long savedProductId = productService.registerProduct(ProductRequestFixture.request);
            final Long savedCartId = cartService.putInCart(savedProductId, 1L);
            final String encodedAuth = new String(Base64.getEncoder().encode("a@a.com".getBytes()));

            cartRemoveApi(encodedAuth, String.valueOf(savedCartId))
                    .statusCode(HttpStatus.UNAUTHORIZED.value())
                    .body("message", equalTo("유효하지 않은 인증 정보입니다."));
        }

        @Test
        @DisplayName("ID로 변환할 수 없는 타입이라면 400 상태를 반환한다.")
        void removeCartItemWithInvalidIDType() {
            final String encodedAuth = new String(Base64.getEncoder().encode("a@a.com:password".getBytes()));

            cartRemoveApi(encodedAuth, "hello")
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        private ValidatableResponse cartRemoveApi(final String auth, final String cartId) {
            return RestAssured.given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Basic " + auth)
                    .when()
                    .delete("/carts/" + cartId)
                    .then();
        }
    }
}
