package com.zb.fresh_api.api.common;

import com.zb.fresh_api.api.dto.KakaoOAuthResponse;
import lombok.Builder;

@Builder
public record GlobalResponse<T> (
    boolean success,
    String code,
    String message,
    T data
){
    public static GlobalResponse<KakaoOAuthResponse> ofSuccess(KakaoOAuthResponse response, String 카카오_로그인_성공) {
        return GlobalResponse.<KakaoOAuthResponse>builder().build();
    }
}
