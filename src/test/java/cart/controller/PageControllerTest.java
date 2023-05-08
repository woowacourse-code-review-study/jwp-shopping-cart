package cart.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cart.dto.MemberResponse;
import cart.dto.ProductResponse;
import cart.service.MemberService;
import cart.service.ProductService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(PageController.class)
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private MemberService memberService;

    @DisplayName("인덱스 페이지를 상품 리스트와 함께 조회한다")
    @Test
    void indexPageTest() throws Exception {
        List<ProductResponse> products = List.of(
                new ProductResponse(1L, "boxster", "https://boxster.com", 10000),
                new ProductResponse(2L, "gitchan", "https://gitchan.com", 20000)
        );

        when(productService.findProducts()).thenReturn(products);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("products", hasSize(2)));
    }

    @DisplayName("관리자 페이지를 상품 리스트와 함께 조회한다")
    @Test
    void adminPageTest() throws Exception {
        List<ProductResponse> products = List.of(
                new ProductResponse(1L, "boxster", "https://boxster.com", 10000),
                new ProductResponse(2L, "gitchan", "https://gitchan.com", 20000)
        );

        when(productService.findProducts()).thenReturn(products);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("products", hasSize(2)));
    }

    @DisplayName("회원 페이지를 전체 회원과 함께 조회한다")
    @Test
    void settingPageTest() throws Exception {
        List<MemberResponse> members = List.of(
                new MemberResponse("boxster@email.com", "boxster"),
                new MemberResponse("gitchan@email.com", "gitchan"));

        when(memberService.findMembers()).thenReturn(members);

        mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("members", hasSize(2)));
    }
}
