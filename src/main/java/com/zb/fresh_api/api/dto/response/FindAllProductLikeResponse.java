package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "좋아요한 상품 목록 응답")
public record FindAllProductLikeResponse(
    List<ProductByLikeDto> productList
) {
    public static FindAllProductLikeResponse fromEntities(List<Product> productList) {
        return new FindAllProductLikeResponse(
            productList.stream().map(ProductByLikeDto::fromEntity).toList()
        );
    }
}
