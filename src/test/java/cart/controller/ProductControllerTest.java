package cart.controller;

import cart.dao.MemberDao;
import cart.dto.request.ProductRequest;
import cart.fixture.ImageFixture;
import cart.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static cart.fixture.ProductFixture.CHICKEN;
import static cart.fixture.ProductFixture.SNACK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(ProductController.class)
@Import({MemberDao.class})
@MockBean(JdbcTemplate.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void 상품_저장_요청() throws Exception {
        ProductRequest request = new ProductRequest(ImageFixture.url, "name", 1000);
        String jsonRequest = objectMapper.writeValueAsString(request);

        when(productService.findAll())
                .thenReturn(List.of(SNACK.RESPONSE));

        mockMvc.perform(post("/products")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void 상품_수정_요청() throws Exception {
        ProductRequest request = new ProductRequest(ImageFixture.url, "name", 1000);
        String jsonRequest = objectMapper.writeValueAsString(request);

        when(productService.update(any(), any()))
                .thenReturn(CHICKEN.RESPONSE);

        mockMvc.perform(put("/products/{id}", 1L)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 상품_삭제_요청() throws Exception {
        mockMvc.perform(delete("/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 상품_조회_요청() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
