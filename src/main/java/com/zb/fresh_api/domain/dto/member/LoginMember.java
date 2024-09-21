package com.zb.fresh_api.domain.dto.member;

import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.Provider;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인된 유저의 정보")
public record LoginMember(
        @Schema(description = "고유 번호", example = "14")
        Long id,

        @Schema(description = "이메일", example = "fresh2you@naver.com")
        String email,

        @Schema(description = "닉네임", example = "농부")
        String nickname,

        @Schema(description = "가입 경로", example = "KAKAO")
        Provider provider
) {
        public static LoginMember fromEntity(Member member) {
                return new LoginMember(member.getId(), member.getEmail(), member.getNickname(), member.getProvider());
        }
}
