package com.zb.fresh_api.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("java:S2386")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    public static final String[] ALLOW_ORIGINS = {
            "http://localhost:8080",
            "http://localhost:3000",
            "https://api.fresh2you.shop",
            "https://fresh2you.shop",
            "https://www.fresh2you.shop",
    };

    public static final String[] SWAGGER_PATH = {
            "/swagger-ui/**",
            "/api-docs/**",
    };

    public static final String[] WEB_IGNORE_PATH = {
            "/health",
    };

    public static final String[] PERMIT_ALL_PATH = {
            /**
             * Health
            */
            "/health",

            /**
             * Member
            */
            "/v1/api/members/signup",
            "/v1/api/members/login",
            "/v1/api/members/login/**",
            "/v1/api/members/check-nickname",
            "/v1/api/members/check-email",

            /**
             * Terms
            */
            "/v1/api/terms/**",

            /**
             * Recommend
            */
            "/v1/api/recommendations/**",

            /**
             * Auth
            */
            "/v1/api/auth/**",

            /**
             * Web Socket
            */
            "/ws/**"
    };

}
