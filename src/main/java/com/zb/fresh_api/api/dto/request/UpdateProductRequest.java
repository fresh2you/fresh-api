package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "상품 수정 요청")
public record UpdateProductRequest (
    @Schema(description = "상품명")
    String name,

    @Schema(description = "상품 설명")
    String description,

    @Schema(description = "재고")
    Integer quantity,

    @Schema(description = "가격")
    BigDecimal price,

    @Schema(description = "카테고리 ID")
    Long categoryId
){}
