package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "배송지 추가 요청")
public record AddDeliveryAddressRequest(
        @Schema(description = "받는 사람 이름", example = "홍길동")
        String recipientName,

        @Schema(description = "전화번호", example = "01012345678")
        String phone,

        @Schema(description = "주소", example = "경기도 광명시 철산동")
        String address,

        @Schema(description = "상세 주소", example = "땡땡 아파트 104동 705호")
        String detailedAddress,

        @Schema(description = "우편 번호", example = "14888")
        String postalCode,

        @Schema(description = "기본 배송지 여부", example = "true")
        boolean isDefault
) {
}
