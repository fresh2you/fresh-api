package com.zb.fresh_api.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisConstants {
    public static final String PHONE_PREFIX = "sms:";
    public static final String EMAIL_PREFIX = "email:";
    public static final String ATTEMPT_PREFIX = "attempt:";
    public static final int CODE_EXPIRATION_TIME = 3 * 60;
    public static final int ATTEMPT_EXPIRATION_TIME = 24 * 60 * 60;
    public static final int MAX_ATTEMPTS = 5;
}
