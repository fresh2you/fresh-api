package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 삭제 요청")
public record DeleteProductRequest(
    @Schema(description = "삭제할 상품 ID")
    Long productId
) {

}
