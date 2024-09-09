package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

@Schema(description = "프로필 변경 요청")
public record UpdateProfileRequest(
        @Max(value = 20, message = "닉네임이 20글자가 초과되었습니다.")
        @NotNull(message = "닉네임이 누락되었습니다.")
        @Schema(description = "닉네임", example = "장아찌")
        String nickname
) {
}
