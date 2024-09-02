package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.common.GlobalResponse;
import com.zb.fresh_api.api.common.ResponseCode;
import com.zb.fresh_api.api.common.exception.CustomException;
import com.zb.fresh_api.api.common.exception.ErrorCode;
import com.zb.fresh_api.api.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
