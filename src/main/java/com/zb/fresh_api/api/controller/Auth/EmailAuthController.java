package com.zb.fresh_api.api.controller.Auth;

import com.zb.fresh_api.api.service.EmailService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "이메일 인증 API",
    description = "이메일 인증에 관련된 API"
)
@RestController
@RequestMapping("/v1/api/auth/email")
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailService emailService;

    @Operation(
        summary = "이메일 인증 전송",
        description = "입력한 이메일로 인증번호를 전송합니다"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Void>> sendSms(@Valid @Email @RequestParam String email) {
        emailService.sendMail(email);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(
        summary = "인증 코드 인증",
        description = "이메일로 인증코드를 통해 인증합니다"
    )
    @GetMapping("/certificate")
    public ResponseEntity<ApiResponse<Void>> certificateSms(@Valid @Email @RequestParam String email,
        @RequestParam String certificationCode) {
        emailService.certificateCode(email, certificationCode);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }
}
