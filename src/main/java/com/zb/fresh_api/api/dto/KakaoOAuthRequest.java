package com.zb.fresh_api.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 OAuth 요청 DTO")
public record KakaoOAuthRequest(
        @Schema(description = "인가 코드", example = "authorization_code") String code,
        @Schema(description = "상태 값", example = "state_value") String state
) {}


