package com.zb.fresh_api.api.service;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import jakarta.annotation.PostConstruct;
import java.security.SecureRandom;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SmsService {

    @Value("${spring.coolsms.apiKey}") // coolsms의 API 키 주입
    private String apiKey;

    @Value("${spring.coolsms.apiSecret}") // coolsms의 API 비밀키 주입
    private String apiSecret;

    @Value("${spring.coolsms.fromNumber}") // 발신자 번호 주입
    private String fromNumber;

    @Value("${spring.coolsms.apiUrl}")
    private String apiUrl;

    DefaultMessageService messageService;
    private final RedisService redisService;

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static final SecureRandom random = new SecureRandom();
    private static final int CODE_LENGTH = 6;


    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, apiUrl);
    }

    /**
     * 문자 인증 코드 전송 로직
     * 1. 현재 전화번호의 요청이 제한 횟수 이상 왔는지 검증
     * 2. 인증 문자 전송
     * 3. 인증 유효시간 설정
     * 4. 인증 문자 전송 횟수 기록
     */
    public void sendSms(String toNumber) {
        if (redisService.hasExceededAttemptLimit(toNumber)) {
            throw new CustomException(ResponseCode.EXCEEDED_CERTIFICATION_ATTEMPS);
        }
        String certificationCode = this.generateRandomString();
        this.sendCertificationCode(toNumber, certificationCode);
        redisService.saveCertificationCode(toNumber, certificationCode);
        redisService.recordAttempt(toNumber);
    }

    /**
     * 문자 인증 코드 인증 로직
     * 1. 휴대전화 인증 내역 조회 후 없으면 에러 발생
     * 2. 인증 코드가 일치하면 성공 로직 구현(미정) 일치하지 않으면 에러 발생
     * 3. redis에서 휴대전화 관련 데이터 삭제
     */
    public void certificateCode(String phone, String certificationCode){
        String smsCertification = redisService.findSmsCertification(phone);
        if(smsCertification == null){
            throw new CustomException(ResponseCode.CERTIFICATION_NOT_FOUND);
        }
        if(certificationCode.equals(smsCertification)){
            // TODO 문자 인증 성공 시 로직 구현
            // System.out.println("문자 인증 성공~");
        }else{
            throw new CustomException(ResponseCode.CERTIFICATION_CODE_NOT_CORRECT);
        }
        redisService.removeSmsCertification(phone);
    }

    // 인증 문자 전송 메서드
    private void sendCertificationCode(String toNumber, String certificationCode) {
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(toNumber);
        message.setText("[Fresh 2 you] \n" + "본인확인 인증번호는 " + certificationCode + "입니다.");
        messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    private String generateRandomString() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return sb.toString();
    }
}
