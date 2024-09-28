package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.dto.recommend.RecommendProductSummary;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "상품 추천 서비스 응답")
public record LoadProductRecommendListResponse(

        @Schema(description = "상품 목록")
        List<RecommendProductSummary> products,

        @Schema(description = "상품 개수")
        int count
) {
}
