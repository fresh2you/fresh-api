package com.zb.fresh_api.api.dto;

import com.zb.fresh_api.api.validation.annotation.PasswordMatch;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@PasswordMatch(password = "password", confirmPassword = "confirmPassword")
public record SignUpRequest (
    @NotBlank(message = "아이디는 필수입니다")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다")
    String password,

    @NotBlank(message = "비밀번호 확인은 필수입니다")
    String confirmPassword,

    @NotBlank(message = "닉네임은 필수입니다")
    String nickname,

    @NotNull(message = "사용자 이용약관 동의은 필수입니다")
    List<@Valid TermsAgreementDto> termsAgreements
){}
