package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.common.GlobalResponse;
import com.zb.fresh_api.api.common.ResponseCode;
import com.zb.fresh_api.api.common.exception.CustomException;
import com.zb.fresh_api.api.common.exception.ErrorCode;
import com.zb.fresh_api.api.dto.SignUpRequest;
import com.zb.fresh_api.api.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "사용자 API",
    description = "사용자와 관련된 API"
)
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(
        summary = "닉네임 중복 검사",
        description = "중복된 닉네임이 있는지 검사합니다"
    )
    @GetMapping("/check-nickname")
    public GlobalResponse<?> checkNicknameAvailability(
        @RequestParam(name = "nickname") String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            throw new CustomException(ErrorCode.PARAM_NICKNAME_NOT_VALID);
        }
        memberService.nickNameValidate(nickname);
        return GlobalResponse.builder()
            .success(true)
            .message("닉네임 중복 확인 성공(중복X)")
            .code(ResponseCode.SUCCESS.getCode())
            .build();
    }
    @Operation(
        summary = "이메일 중복 검사",
        description = "이메일로 회원가입한 사용자 중 중복된 이메일이 있는지 검사합니다"
    )
    @GetMapping("/check-email")
    public GlobalResponse checkEmailAvailability(
        @RequestParam String email
    ) {
        if (email == null || email.isEmpty()) {
            throw new CustomException(ErrorCode.PARAM_NICKNAME_NOT_VALID);

        }
        memberService.emailValidate(email);
        return GlobalResponse.builder()
            .success(true)
            .message("이메일 중복 확인 성공(중복X)")
            .code(ResponseCode.SUCCESS.getCode())
            .build();
    }

    @PostMapping("/signup")
    public GlobalResponse signUp(@RequestBody @Valid SignUpRequest request) {
        memberService.signUp(request.email(), request.password(), request.nickname());
        return GlobalResponse.builder()
            .success(true)
            .message("회원 가입 성공")
            .code(ResponseCode.SUCCESS.getCode())
            .build();
    }
}
