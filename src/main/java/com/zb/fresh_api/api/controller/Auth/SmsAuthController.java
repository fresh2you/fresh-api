package com.zb.fresh_api.api.controller.Auth;

import com.zb.fresh_api.api.annotation.LoginMember;
import com.zb.fresh_api.api.service.SmsService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import com.zb.fresh_api.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
        summary = "판매자 인증 문자 전송",
        description = "입력한 휴대전화로 인증번호를 전송합니다"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> sendVerificationCode(@RequestParam String phoneNumber,
        @Parameter(hidden = true) @LoginMember Member member) {
        smsService.sendSms(phoneNumber,member);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(
        summary = "판매자 인증 코드 인증",
        description = "휴대전화로 온 인증코드를 통해 인증합니다"
    )
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifySmsCode(@RequestParam String phoneNumber,
        @RequestParam String verificationCode, @Parameter(hidden = true) @LoginMember Member member) {
        smsService.verifyCode(phoneNumber, verificationCode, member);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }
}
