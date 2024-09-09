package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.api.dto.LoginMember;
import com.zb.fresh_api.domain.dto.token.Token;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginResponse(
        @Schema(description = "토큰 정보")
        Token token,

        @Schema(description = "로그인 유저 정보")
        LoginMember loginMember
) {
}
