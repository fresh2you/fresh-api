package com.zb.fresh_api.domain.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record KakaoUser(
        @Schema(description = "회원번호")
        @JsonProperty("id")
        String id,

        @Schema(description = "자동 연결 설정을 비활성화한 경우만 존재, 연결하기 호출의 완료 여부")
        @JsonProperty("has_signed_up")
        Boolean hasSignedUp,

        @Schema(description = "서비스에 연결 완료된 시각, UTC")
        @JsonProperty("connected_at")
        LocalDateTime connectedAt,

        @Schema(description = "카카오싱크 간편가입을 통해 로그인한 시각, UTC")
        @JsonProperty("synched_at")
        LocalDateTime synchedAt,

        @Schema(description = "카카오계정 정보")
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) implements OauthUser {

    @Override
    public String email() {
        return kakaoAccount.email();
    }

}
