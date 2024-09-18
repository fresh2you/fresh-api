package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 수정 응답")
public record UpdateProductResponse (
    @Schema(description = "상품 ID")
    Long productId
){}
