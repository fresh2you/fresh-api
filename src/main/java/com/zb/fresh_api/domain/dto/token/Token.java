package com.zb.fresh_api.domain.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "로그인 유저의 검증을 위한 토큰")
public record Token(
        @Schema(description = "ACCESS 토큰")
        String accessToken,

        @Schema(description = "만료 시간")
        Date accessExpiredAt
) {
        public static Token emptyToken() {
                return new Token(null, null);
        }
}
