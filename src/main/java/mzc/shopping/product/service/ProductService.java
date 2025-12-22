package mzc.shopping.product.service;

import lombok.RequiredArgsConstructor;
import mzc.shopping.product.dto.ProductRequest;
import mzc.shopping.product.dto.ProductResponse;
import mzc.shopping.product.entity.Product;
import mzc.shopping.product.entity.ProductStatus;
import mzc.shopping.product.exception.OutOfStockException;
import mzc.shopping.product.exception.ProductNotFoundException;
import mzc.shopping.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .status(ProductStatus.AVAILABLE)
                .build();

        Product saved = productRepository.save(product);
        return ProductResponse.from(saved);
    }

    // 상품 조회
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ProductResponse.from(product);
    }

    // 전체 상품 조회
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // 카테고리별 조회
    public List<ProductResponse> getProductsByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // 판매중 상품 조회
    public List<ProductResponse> getAvailableProducts() {
        return productRepository.findByStatus(ProductStatus.AVAILABLE).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // 상품 검색
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByNameContaining(keyword).stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    // 상품 수정
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        return ProductResponse.from(product);
    }

    // 상품 삭제
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        productRepository.delete(product);
    }

    // 재고 감소 (주문 시)
    @Transactional
    public ProductResponse decreaseStock(Long id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (product.getStock() < quantity) {
            throw new OutOfStockException(id, quantity, product.getStock());
        }

        product.decreaseStock(quantity);

        // 재고 0이면 품절 처리
        if (product.getStock() == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        }

        return ProductResponse.from(product);
    }

    // 재고 증가 (주문 취소 시)
    @Transactional
    public ProductResponse increaseStock(Long id, int quantity) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.increaseStock(quantity);

        // 품절이었다면 판매중으로 변경
        if (product.getStatus() == ProductStatus.OUT_OF_STOCK) {
            product.setStatus(ProductStatus.AVAILABLE);
        }

        return ProductResponse.from(product);
    }
}