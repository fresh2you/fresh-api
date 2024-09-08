package com.zb.fresh_api.api.controller.Auth;

import com.zb.fresh_api.api.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "휴대전화 인증 API",
    description = "휴대전화 인증에 관련된 API"
)
@RestController
@RequestMapping("/v1/api/auth/sms")
@RequiredArgsConstructor
public class SmsAuthController {

    private final SmsService smsService;

    @Operation(
        summary = "인증 문자 전송",
        description = "입력한 휴대전화로 인증번호를 전송합니다"
    )
    @GetMapping
    public void sendSms(@RequestParam String phoneNumber) {
        smsService.sendSms(phoneNumber);
    }

    @Operation(
        summary = "인증 코드 인증",
        description = "휴대전화로 온 인증코드를 통해 인증합니다"
    )
    @GetMapping("/certificate")
    public void certificateSms(@RequestParam String phoneNumber,
        @RequestParam String certificationCode) {
        smsService.certificateCode(phoneNumber, certificationCode);
    }
}
