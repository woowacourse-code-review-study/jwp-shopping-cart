package cart.controller.product;

import cart.domain.product.Product;
import cart.dto.product.ProductRequest;
import cart.dto.product.ProductResponse;
import cart.service.product.ProductCommandService;
import cart.service.product.ProductQueryService;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductApiController {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    public ProductApiController(final ProductCommandService productCommandService,
            final ProductQueryService productQueryService) {
        this.productCommandService = productCommandService;
        this.productQueryService = productQueryService;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid final ProductRequest productRequest) {
        final Product product = productCommandService.create(
                productRequest.getName(),
                productRequest.getImage(),
                productRequest.getPrice());

        final ProductResponse productResponse = ProductResponse.from(product);
        return ResponseEntity.created(URI.create("/products/" + product.getProductId()))
                .body(productResponse);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        final List<ProductResponse> productResponses = productQueryService.findAll()
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productResponses);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable final long id,
            @RequestBody @Valid final ProductRequest productRequest) {
        final Product product = productCommandService.update(
                id,
                productRequest.getName(),
                productRequest.getImage(),
                productRequest.getPrice());

        final ProductResponse productResponse = ProductResponse.from(product);
        return ResponseEntity.ok(productResponse);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable final long id) {
        productCommandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
