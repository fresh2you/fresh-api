package com.zb.fresh_api.api.service;


import com.zb.fresh_api.api.enums.CertificationType;
import com.zb.fresh_api.api.utils.CertificationCodeUtil;
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
        if (redisUtil.hasExceededAttemptLimit(email, CertificationType.EMAIL)) {
            throw new CustomException(ResponseCode.EXCEEDED_CERTIFICATION_ATTEMPS);
        }
        String certificationCode = CertificationCodeUtil.generateRandomString();
        emailUtil.sendCertificationCode(email, certificationCode);
        redisUtil.saveCertificationCode(email, certificationCode, CertificationType.EMAIL);
        redisUtil.recordAttempt(email, CertificationType.EMAIL);
    }

    public void certificateCode(String email, String certificationCode) {
        String findCertification = redisUtil.findSmsCertification(email, CertificationType.EMAIL);
        if (findCertification == null) {
            throw new CustomException(ResponseCode.CERTIFICATION_NOT_FOUND);
        }
        if (!certificationCode.equals(findCertification)) {
            throw new CustomException(ResponseCode.CERTIFICATION_CODE_NOT_CORRECT);
        }
        redisUtil.removeSmsCertification(email, CertificationType.EMAIL);
    }
}
