package com.zb.fresh_api.domain.dto.member;

import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.point.Point;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 정보와 포인트 정보")
public record MemberWithPoint(

        Member member,

        Point point
) {
}
