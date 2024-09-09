package com.zb.fresh_api.domain.dto.token;

public interface OauthToken {
    String tokenType();
    String accessToken();
    int expiresIn();
    String refreshToken();
    String scope();
    int refreshTokenExpiresIn();
}
