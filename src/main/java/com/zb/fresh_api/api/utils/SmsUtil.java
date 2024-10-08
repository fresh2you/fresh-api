package com.zb.fresh_api.api.utils;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import jakarta.annotation.PostConstruct;
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

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, apiUrl);
    }


    // 인증 문자 전송 메서드
    public void sendVerificationCode(String toNumber, String verificationCode) {
        Message message = new Message();
        message.setFrom(fromNumber);
        message.setTo(toNumber);
        message.setText("[Fresh 2 you] \n" + "본인확인 인증번호는 " + verificationCode + "입니다.");
        try {
            messageService.sendOne(new SingleMessageSendingRequest(message));
        }catch (Exception e) {
            throw new CustomException(ResponseCode.NOT_ENOUGH_BALANCE);
        }
    }

}
