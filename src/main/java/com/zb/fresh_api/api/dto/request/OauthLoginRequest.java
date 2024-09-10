package com.zb.fresh_api.api.dto.request;

import com.zb.fresh_api.domain.enums.member.Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "소셜 로그인 요청")
public record OauthLoginRequest(
        @NotBlank(message = "인가코드가 누락되었습니다.")
        String code,

        @NotNull(message = "리다이렉트 URI가 누락되었습니다.")
        String redirectUri,

        @NotNull(message = "가입 경로가 누락되었습니다.")
        Provider provider
) {
}
