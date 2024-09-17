package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "좋아요한 상품 응답")
public record ProductByLikeDto(
    @Schema(description = "상품 ID")
    Long productId,

    @Schema(description = "상품명")
    String productName,

    @Schema(description = "사진")
    String imageUrl,

    @Schema(description = "가격")
    BigDecimal price
) {
    public static ProductByLikeDto fromEntity(Product product){
        return new ProductByLikeDto(product.getId(),product.getName(), product.getProductImage(),product.getPrice());
    }
}
