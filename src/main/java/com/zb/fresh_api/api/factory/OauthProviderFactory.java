package com.zb.fresh_api.api.factory;

import com.zb.fresh_api.api.provider.oauth.KakaoProvider;
import com.zb.fresh_api.api.provider.oauth.OauthProvider;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.member.OauthUser;
import com.zb.fresh_api.domain.dto.token.OauthToken;
import com.zb.fresh_api.domain.enums.member.Provider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Component
public class OauthProviderFactory {

    private final Map<Provider, OauthProvider> providerMap;
    private final KakaoProvider kakaoProvider;

    public OauthProviderFactory(
            KakaoProvider kakaoProvider
    ) {
        providerMap = new EnumMap<>(Provider.class);
        this.kakaoProvider = kakaoProvider;
        initialize();
    }

    private void initialize() {
        providerMap.put(Provider.KAKAO, kakaoProvider);
    }

    private OauthProvider getOauthProvider(Provider provider) {
        OauthProvider oAuthProvider = providerMap.get(provider);
        if (Objects.isNull(oAuthProvider)) {
            throw new CustomException(ResponseCode.NOT_FOUND_OAUTH_PROVIDER);
        }

        return oAuthProvider;
    }

    public String getAccessToken(Provider provider, String redirectUri, String code) {
        return getOauthToken(provider, redirectUri, code).accessToken();
    }

    private OauthToken getOauthToken(Provider provider, String redirectUri, String code) {
        return getOauthProvider(provider).getOauthToken(redirectUri, code);
    }

    public OauthUser getOauthUser(Provider provider, String accessToken) {
        return getOauthProvider(provider).getOauthUser(accessToken);
    }

}
