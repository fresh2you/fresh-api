package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배송지 추가 응답")
public record AddDeliveryAddressResponse(
        @Schema(description = "설정된 배송지 개수", example = "3")
        Long addressCount
) {
}
