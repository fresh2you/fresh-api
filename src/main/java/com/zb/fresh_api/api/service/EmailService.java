package com.zb.fresh_api.api.service;


import com.zb.fresh_api.api.enums.VerificationType;
import com.zb.fresh_api.api.utils.VerificationCodeUtil;
import com.zb.fresh_api.api.utils.EmailUtil;
import com.zb.fresh_api.api.utils.RedisUtil;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RedisUtil redisUtil;
    private final EmailUtil emailUtil;


    public void sendMail(String email) {
        if (redisUtil.hasExceededAttemptLimit(email, VerificationType.EMAIL)) {
            throw new CustomException(ResponseCode.EXCEEDED_VERIFICATION_ATTEMPS);
        }
        String verificationCode = VerificationCodeUtil.generateRandomString();
        emailUtil.sendVerificationCode(email, verificationCode);
        redisUtil.saveVerificationCode(email, verificationCode, VerificationType.EMAIL);
        redisUtil.recordAttempt(email, VerificationType.EMAIL);
    }

    public void verifyCode(String email, String verificationCode) {
        String findVerification = redisUtil.findSmsVerification(email, VerificationType.EMAIL);
        if (findVerification == null) {
            throw new CustomException(ResponseCode.VERIFICATION_NOT_FOUND);
        }
        if (!verificationCode.equals(findVerification)) {
            throw new CustomException(ResponseCode.VERIFICATION_CODE_NOT_CORRECT);
        }
        redisUtil.removeSmsVerification(email, VerificationType.EMAIL);
    }
}
