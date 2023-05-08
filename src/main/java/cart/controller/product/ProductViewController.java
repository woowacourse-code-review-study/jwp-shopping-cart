package cart.controller.product;

import cart.dto.product.ProductResponse;
import cart.service.product.ProductQueryService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductViewController {

    private final ProductQueryService productSearchService;

    public ProductViewController(final ProductQueryService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @GetMapping("/")
    public String getIndexPage(final Model model) {
        final List<ProductResponse> productResponses = productSearchService.findAll()
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        model.addAttribute("products", productResponses);
        return "index";
    }

    @GetMapping("/admin")
    public String getAdminPage(final Model model) {
        final List<ProductResponse> productResponses = productSearchService.findAll()
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        model.addAttribute("products", productResponses);
        return "admin";
    }

}
