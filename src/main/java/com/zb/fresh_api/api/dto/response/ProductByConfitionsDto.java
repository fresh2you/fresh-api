package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ProductByConfitionsDto(
    @Schema(description = "상품 ID")
    Long productId,

    @Schema(description = "판매자이름")
    String sellerName,
    
    @Schema(description = "상품명")
    String productName,
    
    @Schema(description = "상품 설명")
    String productDescription,
    
    @Schema(description = "수량")
    int quantity,
    
    @Schema(description = "가격")
    BigDecimal price,
    
    @Schema(description = "사진")
    String imageUrl
) {
    public static ProductByConfitionsDto fromEntity(Product product){
        return new ProductByConfitionsDto(product.getId(),
            product.getMember().getNickname(),
            product.getName(),
            product.getDescription(),
            product.getQuantity(),
            product.getPrice(),
            product.getProductImage());
    }
}
