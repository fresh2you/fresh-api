package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email(message = "이메일 주소 형식이 잘못되었습니다.")
        @Schema(description = "로그인할 이메일", defaultValue = "fresh2you@naver.com")
        String email,

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?!.*\\s).+$"
                , message = "비밀번호는 영어와 숫자를 혼용해야 하며 공백은 사용할 수 없습니다.")
        @Size(min = 8, max = 16, message = "비밀번호는 최소 8글자 이상 최대 16글자 이하로 작성해야 합니다.")
        @Schema(description = "비밀번호", defaultValue = "fresh2you!")
        String password
) {
}
