package com.zb.fresh_api.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 OAuth 응답 DTO")
public record KakaoOAuthResponse(
        @Schema(description = "qwer1234") String id,
        @Schema(description = "qwer1234@naver.com") String email,
        @Schema(description = "rewq4321") String nickname
) {}
