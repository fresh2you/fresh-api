package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.dto.KakaoOAuthRequest;
import com.zb.fresh_api.api.dto.KakaoOAuthResponse;
import com.zb.fresh_api.api.service.kakaoOAuthService;
import com.zb.fresh_api.common.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class KakaoOAuthController {

    private final kakaoOAuthService kakaoOAuthService;

    @PostMapping("/kakao")
    public GlobalExceptionHandler<KakaoOAuthResponse> handleKakaoLogin(@RequestBody KakaoOAuthRequest request) {
        KakaoOAuthResponse response = kakaoOAuthService.getKakaoUserInfo(request);
        return GlobalExceptionHandler.ofSuccess(response, "카카오 로그인 성공");
    }
}