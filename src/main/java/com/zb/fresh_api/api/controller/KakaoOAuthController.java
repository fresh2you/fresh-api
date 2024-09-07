package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.dto.KakaoOAuthRequest;
import com.zb.fresh_api.api.dto.KakaoOAuthResponse;
import com.zb.fresh_api.api.service.KakaoOAuthService;
import com.zb.fresh_api.api.common.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @PostMapping("/kakao")
    public GlobalResponse<KakaoOAuthResponse> handleKakaoLogin(@RequestBody KakaoOAuthRequest request) {
        KakaoOAuthResponse response = kakaoOAuthService.getKakaoUserInfo(request);
        return GlobalResponse.ofSuccess(response, "카카오 로그인 성공");
    }
}
