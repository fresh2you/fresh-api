package com.zb.fresh_api.domain.dto.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 토큰 정보")
public record KakaoToken(
        @Schema(description = "토큰 타입", example = "bearer")
        @JsonProperty("token_type")
        String tokenType,

        @Schema(description = "갱신된 사용자 엑세스 토큰")
        @JsonProperty("access_token")
        String accessToken,

        @Schema(description = "엑세스 토큰 만료 시간(초)")
        @JsonProperty("expires_in")
        int expiresIn,

        @Schema(description = "갱신된 사용자 리프레시 토큰")
        @JsonProperty("refresh_token")
        String refreshToken,

        @Schema(description = "갱신된 사용자 리프레시 토큰, 기존 리프레시 토큰의 유효기간이 1개월 미만인 경우에만 갱신")
        @JsonProperty("refresh_token_expires_in")
        int refreshTokenExpiresIn,

        @Schema(description = "인증된 사용자의 정보 조회 권한 범위", example = "account_email profile")
        @JsonProperty("scope")
        String scope
) implements OauthToken {
}
