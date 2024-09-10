package com.zb.fresh_api.api.provider.oauth;

import com.zb.fresh_api.api.client.KakaoApiClient;
import com.zb.fresh_api.api.client.KakaoAuthClient;
import com.zb.fresh_api.common.constants.AuthConstants;
import com.zb.fresh_api.domain.dto.member.OauthUser;
import com.zb.fresh_api.domain.dto.token.OauthToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoProvider implements OauthProvider {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoAuthClient kakaoAuthClient;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Override
    public OauthToken getOauthToken(String redirectUri, String code) {
        return kakaoAuthClient.generateOAuthToken(
                grantType,
                clientId,
                redirectUri,
                code,
                clientSecret
        );
    }

    @Override
    public OauthUser getOauthUser(String accessToken) {
        return kakaoApiClient.getUserInfo(getBearerToken(accessToken));
    }

    private String getBearerToken(String accessToken) {
        return AuthConstants.TOKEN_PREFIX + accessToken;
    }

}
