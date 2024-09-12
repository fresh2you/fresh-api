package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "제품 상세 조회 응답")
public record GetProductDetailResponse (
    @Schema(description = "상품명")
    String productName,

    @Schema(description = "판매자명")
    String sellerName,

    @Schema(description = "가격")
    BigDecimal price,

    @Schema(description = "재고")
    int quantity,

    @Schema(description = "제품 설명")
    String description,

    @Schema(description = "제품 url")
    String imageUrl
){

    public static GetProductDetailResponse fromEntity(Product product) {
        return new GetProductDetailResponse(
            product.getName(), product.getMember().getNickname(),
            product.getPrice(), product.getQuantity(), product.getDescription(),
            product.getProductImage()
        );
    }
}
