package com.zb.fresh_api.domain.dto.member;

import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.point.Point;
import com.zb.fresh_api.domain.enums.member.Provider;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "로그인된 유저의 정보")
public record OauthLoginMember(
        @Schema(description = "고유 번호", example = "14")
        Long id,

        @Schema(description = "이메일", example = "fresh2you@naver.com")
        String email,

        @Schema(description = "닉네임", example = "농부")
        String nickname,

        @Schema(description = "가입 경로(소셜)", example = "KAKAO")
        Provider provider,

        @Schema(description = "소셜 고유 번호", example = "210412")
        String providerId,

        @Schema(description = "프로필 이미지")
        String profileImage,

        @Schema(description = "판매자 여부")
        Boolean isSeller,

        @Schema(description = "포인트", example = "농부")
        BigDecimal point
) {
        public static OauthLoginMember fromSignupMember(final Member member, final Point point) {
                return new OauthLoginMember(member.getId(), member.getEmail(), member.getNickname(), member.getProvider(), member.getProviderId(), member.getProfileImage(), member.isSeller(), point.getBalance());
        }

        public static OauthLoginMember beforeSignupMember(final String email, final String nickname, final Provider provider, final String providerId) {
                return new OauthLoginMember(null, email, nickname, provider, providerId, null, null, java.math.BigDecimal.ZERO);
        }
}
