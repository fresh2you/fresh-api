package com.zb.fresh_api.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zb.fresh_api.domain.dto.member.OauthLoginMember;
import com.zb.fresh_api.domain.dto.token.Token;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record OauthLoginResponse(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Schema(description = "토큰 정보")
        Token token,

        @Schema(description = "소셜 로그인 유저 정보")
        OauthLoginMember loginMember,

        @Schema(description = "서비스 회원 가입 여부")
        boolean isSignup
) {
}
