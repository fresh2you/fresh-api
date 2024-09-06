package com.zb.fresh_api.api.utils;


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
public class SmsUtil {

    @Value("${spring.coolsms.apiKey}") // coolsms의 API 키 주입
    private String apiKey;

    @Value("${spring.coolsms.apiSecret}") // coolsms의 API 비밀키 주입
    private String apiSecret;

    @Value("${spring.coolsms.fromNumber}") // 발신자 번호 주입
    private String fromNumber;

    @Value("${spring.coolsms.apiUrl}")
    private String apiUrl;

    DefaultMessageService messageService;

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

    public String generateRandomString() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return sb.toString();
    }

    // 인증 문자 전송 메서드
    public void sendCertificationCode(String toNumber, String certificationCode) {
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(toNumber);
        message.setText("[Fresh 2 you] \n" + "본인확인 인증번호는 " + certificationCode + "입니다.");
        messageService.sendOne(new SingleMessageSendingRequest(message));
    }

}
