package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.common.response.ApiResponse;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.api.dto.SignUpRequest;
import com.zb.fresh_api.api.service.MemberService;
import com.zb.fresh_api.domain.enums.member.Provider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/v1/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(
        summary = "닉네임 중복 검사",
        description = "중복된 닉네임이 있는지 검사합니다"
    )
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<Void>> checkNicknameAvailability(
        @RequestParam(name = "nickname") String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            throw new CustomException(ResponseCode.PARAM_NICKNAME_NOT_VALID);
        }
        memberService.nickNameValidate(nickname);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(
        summary = "이메일 중복 검사",
        description = "이메일로 회원가입한 사용자 중 중복된 이메일이 있는지 검사합니다"
    )
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Void>> checkEmailAvailability(
        @RequestParam String email
    ) {
        if (email == null || email.isEmpty()) {
            throw new CustomException(ResponseCode.PARAM_NICKNAME_NOT_VALID);

        }
        memberService.emailValidate(email, Provider.EMAIL);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    
    @Operation(
        summary = "회원 가입",
        description = "이메일회원가입, Oauth2회원가입에 사용되는 API입니다"
    )
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody @Valid SignUpRequest request) {
        memberService.signUp(request.email(), request.password(), request.nickname(), request.termsAgreements()
        ,request.provider(), request.providerId());
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

}
