package com.zb.fresh_api.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("java:S2386")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    public static final String[] ALLOW_ORIGINS = {
            "http://localhost:8080",
    };

    public static final String[] SWAGGER_PATH = {
            "/swagger-ui/**", "/api-docs/**",
    };

    public static final String[] WEB_IGNORE_PATH = {
            "/health",
    };

    public static final String[] PERMIT_ALL_PATH = {
            "/v1/api/members/signup", "/v1/api/members/login/**"
    };

}
