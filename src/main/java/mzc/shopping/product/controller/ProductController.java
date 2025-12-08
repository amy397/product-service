package mzc.shopping.product.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mzc.shopping.product.dto.ProductRequest;
import mzc.shopping.product.dto.ProductResponse;
import mzc.shopping.product.dto.StockRequest;
import mzc.shopping.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 상품 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    // 전체 상품 조회
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 카테고리별 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // 판매중 상품 조회
    @GetMapping("/available")
    public ResponseEntity<List<ProductResponse>> getAvailableProducts() {
        return ResponseEntity.ok(productService.getAvailableProducts());
    }

    // 상품 검색
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    // 상품 수정
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // 재고 감소 (Order Service에서 호출)
    @PostMapping("/{id}/stock/decrease")
    public ResponseEntity<ProductResponse> decreaseStock(
            @PathVariable Long id,
            @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(productService.decreaseStock(id, request.getQuantity()));
    }

    // 재고 증가 (주문 취소 시)
    @PostMapping("/{id}/stock/increase")
    public ResponseEntity<ProductResponse> increaseStock(
            @PathVariable Long id,
            @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(productService.increaseStock(id, request.getQuantity()));
    }

    // 헬스체크
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Product Service is running!");
    }
}