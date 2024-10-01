package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.dto.token.Token;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원가입 응답")
public record SignupResponse(

        @Schema(description = "회원 고유 번호", example = "24")
        Long memberId,

        @Schema(description = "토큰 정보, 소셜 로그인을 통한 회원 가입일 경우에만 제공")
        Token token
) {
}
