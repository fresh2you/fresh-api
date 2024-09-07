package com.zb.fresh_api.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 OAuth 응답 DTO")
public record KakaoOAuthResponse(
        @Schema(description = "사용자 ID", example = "qwer1234") String id,
        @Schema(description = "사용자 이메일", example = "qwer1234@naver.com") String email,
        @Schema(description = "사용자 닉네임", example = "rewq4321") String nickname
) {}