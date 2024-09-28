package com.zb.fresh_api.domain.dto.recommend;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "상품 추천 목록 조회시 사용되는 상품 정보")
public record RecommendProductSummary(

        @Schema(description = "상품명")
        String productName,

        @Schema(description = "상품 썸네일")
        String productImage,

        @Schema(description = "판매자명")
        String sellerName,

        @Schema(description = "판매 가격")
        BigDecimal price,

        @Schema(description = "설명 (최대 20글자)")
        String description

//        @Schema(description = "상품 설명 글자 제한 초과 여부")
//        boolean descriptionLengthExceeded
) {
}
