package mzc.shopping.product.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "상품명은 필수입니다")
    @Size(max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "가격은 필수입니다")
    @DecimalMin(value = "0", message = "가격은 0 이상이어야 합니다")
    private BigDecimal price;

    @NotNull(message = "재고는 필수입니다")
    @Min(value = 0, message = "재고는 0 이상이어야 합니다")
    private Integer stock;

    @Size(max = 50)
    private String category;
}