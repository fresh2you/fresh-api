package com.zb.fresh_api.domain.dto.recommend;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "판매자별 구매 이력 조회")
public record SellerProductOrder(

        @Schema(description = "판매자 회원 고유 번호")
        Long sellerId,

        @Schema(description = "구매 횟수")
        Long count
) {
}
