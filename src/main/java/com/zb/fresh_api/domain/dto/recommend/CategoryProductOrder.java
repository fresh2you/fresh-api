package com.zb.fresh_api.domain.dto.recommend;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리별 구매 이력 조회")
public record CategoryProductOrder(

        @Schema(description = "카테고리 고유 번호")
        Long categoryId,

        @Schema(description = "구매 횟수")
        Long count
) {
}
