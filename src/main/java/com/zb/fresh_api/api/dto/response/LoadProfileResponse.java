package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.dto.member.LoginMember;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 정보 조회 응답")
public record LoadProfileResponse(

        LoginMember loginMember
) {
}
