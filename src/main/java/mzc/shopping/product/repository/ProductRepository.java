package mzc.shopping.product.repository;

import mzc.shopping.product.entity.Product;
import mzc.shopping.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 카테고리별 조회
    List<Product> findByCategory(String category);

    // 상태별 조회
    List<Product> findByStatus(ProductStatus status);

    // 카테고리 + 상태 조회
    List<Product> findByCategoryAndStatus(String category, ProductStatus status);

    // 상품명 검색 (포함)
    List<Product> findByNameContaining(String keyword);

    // 판매중인 상품만 조회
    List<Product> findByStatusOrderByCreatedAtDesc(ProductStatus status);
}