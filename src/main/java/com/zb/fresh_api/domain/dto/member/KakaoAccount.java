package com.zb.fresh_api.domain.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record KakaoAccount(
//        @JsonProperty("has_email")
//        boolean hasEmail,

        @Schema(description = "사용자 동의 시 카카오계정 대표 이메일 제공 가능")
        @JsonProperty("email_needs_agreement")
        Boolean emailNeedsAgreement,

        @Schema(description = "이메일 유효 여부")
        @JsonProperty("is_email_valid")
        Boolean isEmailValid,

        @Schema(description = "이메일 인증 여부")
        @JsonProperty("is_email_verified")
        Boolean isEmailVerified,

        @Schema(description = "이메일")
        @JsonProperty("email")
        String email
) {
}
