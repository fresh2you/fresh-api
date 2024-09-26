package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "상품 추천 서비스 요청")
public record LoadProductRecommendListRequest(

        @Min(value = 1, message = "노출 가능한 목록의 개수는 최소 1개입니다.")
        @Max(value = 10, message = "노출 가능한 목록의 개수는 최대 10개입니다.")
        @NotNull(message = "노출 가능 목록이 누락되었습니다.")
        @Schema(description = "노출 가능한 목록의 개수")
        int size
) {
}
