package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.KakaoOAuthRequest;
import com.zb.fresh_api.api.dto.KakaoOAuthResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class kakaoOAuthService {

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoOAuthResponse getKakaoUserInfo(KakaoOAuthRequest request) {
        String accessToken = getAccessTokenFromKakao(request.code(), request.state());

        return fetchUserInfoFromKakao(accessToken);
    }

    private String getAccessTokenFromKakao(String code, String state) {

        return "098e084b2db7823ea9e62ebdd2e0c672"; // 실제 액세스 토큰 RestAPI키
    }

    private KakaoOAuthResponse fetchUserInfoFromKakao(String accessToken) {
        // 액세스 토큰으로 사용자 정보를 얻기 위한 API 호출
        return new KakaoOAuthResponse("user_id", "user_email", "user_nickname");
    }
}
