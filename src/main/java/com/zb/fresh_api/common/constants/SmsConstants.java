package com.zb.fresh_api.common.constants;

import java.security.SecureRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SmsConstants {
    public static final String NUMBER = "0123456789";
    public static final SecureRandom random = new SecureRandom();
    public static final int CODE_LENGTH = 6;

}
