package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.enums.CertificationType;
import com.zb.fresh_api.api.utils.CertificationCodeUtil;
import com.zb.fresh_api.api.utils.RedisUtil;
import com.zb.fresh_api.api.utils.SmsUtil;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final RedisUtil redisUtil;
    private final SmsUtil smsUtil;

    /**
     * 문자 인증 코드 전송 로직 1. 현재 전화번호의 요청이 제한 횟수 이상 왔는지 검증
     * 2. 인증 문자 전송
     * 3. 인증 유효시간 설정
     * 4. 인증 문자 전송 횟수 기록
     */
    public void sendSms(String toNumber) {
        if (redisUtil.hasExceededAttemptLimit(toNumber, CertificationType.PHONE)) {
            throw new CustomException(ResponseCode.EXCEEDED_CERTIFICATION_ATTEMPS);
        }
        String certificationCode = CertificationCodeUtil.generateRandomString();
        smsUtil.sendCertificationCode(toNumber, certificationCode);
        redisUtil.saveCertificationCode(toNumber, certificationCode, CertificationType.PHONE);
        redisUtil.recordAttempt(toNumber,CertificationType.PHONE);
    }

    /**
     * 문자 인증 코드 인증 로직
     * 1. 휴대전화 인증 내역 조회 후 없으면 에러 발생
     * 2. 인증 코드가 일치하면 성공 로직 구현(미정) 일치하지 않으면 에러 발생
     * 3. redis에서 휴대전화 관련 데이터 삭제
     */
    public void certificateCode(String phone, String certificationCode) {
        String smsCertification = redisUtil.findSmsCertification(phone, CertificationType.PHONE);
        if (smsCertification == null) {
            throw new CustomException(ResponseCode.CERTIFICATION_NOT_FOUND);
        }
        if (certificationCode.equals(smsCertification)) {
            // TODO 문자 인증 성공 시 로직 구현
//             System.out.println("문자 인증 성공~");
        } else {
            throw new CustomException(ResponseCode.CERTIFICATION_CODE_NOT_CORRECT);
        }
        redisUtil.removeSmsCertification(phone,CertificationType.PHONE);
    }
}
