package com.zb.fresh_api.api.utils;

import java.security.SecureRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationCodeUtil {
    public static final String NUMBER = "0123456789";
    public static final SecureRandom random = new SecureRandom();
    public static final int CODE_LENGTH = 6;

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(VerificationCodeUtil.CODE_LENGTH);
        for (int i = 0; i < VerificationCodeUtil.CODE_LENGTH; i++) {
            int rndCharAt = random.nextInt(VerificationCodeUtil.NUMBER.length());
            char rndChar = VerificationCodeUtil.NUMBER.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

}
