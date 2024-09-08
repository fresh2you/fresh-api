package com.zb.fresh_api.api.utils;

import java.security.SecureRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CertificationCodeUtil {
    public static final String NUMBER = "0123456789";
    public static final SecureRandom random = new SecureRandom();
    public static final int CODE_LENGTH = 6;

    public static String generateRandomString() {
        StringBuilder sb = new StringBuilder(CertificationCodeUtil.CODE_LENGTH);
        for (int i = 0; i < CertificationCodeUtil.CODE_LENGTH; i++) {
            int rndCharAt = random.nextInt(CertificationCodeUtil.NUMBER.length());
            char rndChar = CertificationCodeUtil.NUMBER.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
    }

}
