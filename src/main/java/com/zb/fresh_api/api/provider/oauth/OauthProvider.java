package com.zb.fresh_api.api.provider.oauth;


import com.zb.fresh_api.domain.dto.member.OauthUser;
import com.zb.fresh_api.domain.dto.token.OauthToken;

public interface OauthProvider {

    OauthToken getOauthToken(String redirectUri, String code);

    OauthUser getOauthUser(String accessToken);
}
