package com.zb.fresh_api.api.dto;

import com.zb.fresh_api.api.validation.annotation.PasswordMatch;
import jakarta.validation.constraints.NotBlank;

@PasswordMatch(password = "password", confirmPassword = "confirmPassword")
public record SignUpRequest (
    @NotBlank(message = "아이디는 필수입니다")
    String email,

    @NotBlank(message = "비밀번호는 필수입니다")
    String password,

    @NotBlank(message = "비밀번호 확인은 필수입니다")
    String confirmPassword,

    @NotBlank(message = "닉네임은 필수입니다")
    String nickname
){}
