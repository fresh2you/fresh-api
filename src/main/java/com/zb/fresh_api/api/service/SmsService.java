package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.enums.VerificationType;
import com.zb.fresh_api.api.utils.VerificationCodeUtil;
import com.zb.fresh_api.api.utils.RedisUtil;
import com.zb.fresh_api.api.utils.SmsUtil;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.writer.MemberWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final RedisUtil redisUtil;
    private final SmsUtil smsUtil;
    private final MemberReader memberReader;
    private final MemberWriter memberWriter;

    /**
     * 문자 인증 코드 전송 로직 1. 현재 전화번호의 요청이 제한 횟수 이상 왔는지 검증
     * 2. 인증 문자 전송
     * 3. 인증 유효시간 설정
     * 4. 인증 문자 전송 횟수 기록
     */
    public void sendSms(String toNumber) {
        if (redisUtil.hasExceededAttemptLimit(toNumber, VerificationType.PHONE)) {
            throw new CustomException(ResponseCode.EXCEEDED_VERIFICATION_ATTEMPS);
        }

        if(memberReader.existsByPhone(toNumber)){
            throw new CustomException(ResponseCode.PHONE_ALREADY_IN_USE);
        }

        String verificationCode = VerificationCodeUtil.generateRandomString();
        smsUtil.sendVerificationCode(toNumber, verificationCode);
        redisUtil.saveVerificationCode(toNumber, verificationCode, VerificationType.PHONE);
        redisUtil.recordAttempt(toNumber, VerificationType.PHONE);
    }

    /**
     * 문자 인증 코드 인증 로직
     * 1. 휴대전화 인증 내역 조회 후 없으면 에러 발생
     * 2. 인증 코드가 일치하면 성공 로직 구현(미정) 일치하지 않으면 에러 발생
     * 3. redis에서 휴대전화 관련 데이터 삭제
     */
    @Transactional
    public void verifyCode(String phone, String verificationCode, Member member) {
        String findVerification = redisUtil.findSmsVerification(phone, VerificationType.PHONE);
        if (findVerification == null) {
            throw new CustomException(ResponseCode.VERIFICATION_NOT_FOUND);
        }
        if (verificationCode.equals(findVerification)) {
            member.setMemberSeller(phone);
            memberWriter.store(member);
        } else {
            throw new CustomException(ResponseCode.VERIFICATION_CODE_NOT_CORRECT);
        }
        redisUtil.removeSmsVerification(phone, VerificationType.PHONE);
    }
}
