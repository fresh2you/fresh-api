package com.zb.fresh_api.api.controller.Auth;

import com.zb.fresh_api.api.service.SmsService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<Void>> sendVerificationCode(@Valid @Pattern(regexp="(^$|[0-9]{10})")
    @RequestParam String phoneNumber) {
        smsService.sendSms(phoneNumber);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(
        summary = "인증 코드 인증",
        description = "휴대전화로 온 인증코드를 통해 인증합니다"
    )
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifySmsCode(@Valid @Pattern(regexp="(^$|[0-9]{10})")
    @RequestParam String phoneNumber,
        @RequestParam String verificationCode) {
        smsService.verifyCode(phoneNumber, verificationCode);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }
}