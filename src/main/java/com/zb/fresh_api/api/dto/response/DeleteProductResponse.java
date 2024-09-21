package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 삭제 응답")
public record DeleteProductResponse(
    @Schema(description = "삭제된 상품 ID")
    Long productId
) {}
