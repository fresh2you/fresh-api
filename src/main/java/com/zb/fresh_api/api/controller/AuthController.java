package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.dto.KakaoOAuthRequest;
import com.zb.fresh_api.api.dto.KakaoOAuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "카카오 소셜 로그인 API")
@RestController
@RequestMapping("/api")
public class AuthController {

    /**
     * 카카오 로그인 성공 시 호출되는 API
     * @param authentication OAuth2 인증 정보
     * @return KakaoOAuthResponse 로그인 정보 응답
     */
    @Operation(summary = "카카오 로그인 성공", description = "카카오 소셜 로그인이 성공하면 사용자 정보를 반환")
    @GetMapping("/login/success")
    public KakaoOAuthResponse loginSuccess(OAuth2AuthenticationToken authentication) {
        // 사용자 정보를 응답으로 변환하는 로직 구현
        return new KakaoOAuthResponse(
                (String) authentication.getPrincipal().getAttributes().get("id"),
                (String) authentication.getPrincipal().getAttributes().get("email"),
                (String) authentication.getPrincipal().getAttributes().get("nickname")
        );
    }
}