package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.product.ProductLike;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "좋아요 요청 응답")
public record LikeProductResponse(
    @Schema(description = "상품 좋아요 ID")
    Long productLikeId,

    @Schema(description = "상품 ID")
    Long productId,

    @Schema(description = "상품명")
    String productName,

    @Schema(description = "좋아요 누른 시간")
    LocalDateTime likedAt
) {
    public LikeProductResponse(ProductLike productLike) {
        this(productLike.getId(), productLike.getProduct().getId(),
            productLike.getProduct().getName(), productLike.getLiked_at());
    }
}
