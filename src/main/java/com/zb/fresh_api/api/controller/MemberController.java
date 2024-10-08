package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.annotation.LoginMember;
import com.zb.fresh_api.api.dto.SignUpRequest;
import com.zb.fresh_api.api.dto.request.AddDeliveryAddressRequest;
import com.zb.fresh_api.api.dto.request.ChargePointRequest;
import com.zb.fresh_api.api.dto.request.LoginRequest;
import com.zb.fresh_api.api.dto.request.ModifyDeliveryAddressRequest;
import com.zb.fresh_api.api.dto.request.OauthLoginRequest;
import com.zb.fresh_api.api.dto.request.UpdateProfileRequest;
import com.zb.fresh_api.api.dto.response.*;
import com.zb.fresh_api.api.service.MemberService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.member.Provider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
            summary = "회원 가입",
            description = "이메일회원가입, Oauth2회원가입에 사용되는 API입니다"
    )
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@RequestBody @Valid SignUpRequest request) {
        return ApiResponse.success(ResponseCode.SUCCESS, memberService.signUp(request));
    }

    @Operation(
            summary = "로그인",
            description = "이메일 가입 회원의 로그인을 진행한다."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> loginWithEmail(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.success(ResponseCode.SUCCESS, memberService.login(request));
    }

    @Operation(
            summary = "카카오 로그인",
            description = "카카오 로그인을 진행한다."
    )
    @PostMapping("/login/kakao")
    public ResponseEntity<ApiResponse<OauthLoginResponse>> loginWithKakao(@RequestBody @Valid OauthLoginRequest request) {
        return ApiResponse.success(ResponseCode.SUCCESS, memberService.oauthLogin(request));
    }

    @Operation(
            summary = "회원 정보 조회",
            description = "회원의 정보(마이페이지 정보)를 조회한다."
    )
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Void>> checkEmailAvailability(@RequestParam String email) {
        memberService.emailValidate(email, Provider.EMAIL);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(
            summary = "닉네임 중복 검사",
            description = "중복된 닉네임이 있는지 검사합니다"
    )
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse<Void>> checkNicknameAvailability(@RequestParam(name = "nickname") String nickname) {
        memberService.nickNameValidate(nickname);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(
            summary = "회원 정보 조회",
            description = "회원 정보를 조회합니다.(로그인 정보)"
    )
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<LoadProfileResponse>> loadProfile(
            @Parameter(hidden = true) @LoginMember Member loginMember
    ) {
        return ApiResponse.success(ResponseCode.SUCCESS, memberService.loadProfile(loginMember));
    }

    @Operation(
            summary = "프로필 변경",
            description = "회원의 이미지 또는 닉네임을 변경한다."
    )
    @PatchMapping(value = "/profile", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateProfile(
            @Parameter(hidden = true) @LoginMember Member loginMember,
            @Parameter @RequestPart(value = "request", required = false) @Valid UpdateProfileRequest request,
            @Parameter @RequestPart(value = "image", required = false) MultipartFile image) {
        memberService.updateProfile(loginMember, request, image);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(
            summary = "배송지 추가",
            description = "배송지 정보를 추가합니다. (최대3개, 대표1개)"
    )
    @PostMapping("/delivery-addresses")
    public ResponseEntity<ApiResponse<AddDeliveryAddressResponse>> addDeliveryAddress(
            @Parameter(hidden = true) @LoginMember Member loginMember,
            @RequestBody @Valid AddDeliveryAddressRequest request) {
        return ApiResponse.success(ResponseCode.SUCCESS, memberService.addDeliveryAddress(loginMember, request));
    }

    @Operation(
            summary = "배송지 수정",
            description = "배송지 정보를 수정합니다."
    )
    @PatchMapping("/delivery-addresses/{deliveryAddressId}")
    public ResponseEntity<ApiResponse<Void>> modifyDeliveryAddress(
            @Parameter(hidden = true) @LoginMember Member loginMember,
            @PathVariable Long deliveryAddressId,
            @RequestBody @Valid ModifyDeliveryAddressRequest request) {
        memberService.modifyDeliveryAddress(loginMember, deliveryAddressId, request);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(
            summary = "배송지 삭제",
            description = "배송지 정보를 삭제합니다."
    )
    @DeleteMapping("/delivery-addresses/{deliveryAddressId}")
    public ResponseEntity<ApiResponse<Void>> deleteDeliveryAddress(
            @Parameter(hidden = true) @LoginMember Member loginMember,
            @PathVariable Long deliveryAddressId) {
        memberService.deleteDeliveryAddress(loginMember, deliveryAddressId);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(
            summary = "배송지 전체 삭제",
            description = "배송지 정보를 전체 삭제합니다."
    )
    @DeleteMapping("/delivery-addresses")
    public ResponseEntity<ApiResponse<Void>> deleteDeliveryAddress(
            @Parameter(hidden = true) @LoginMember Member loginMember) {
        memberService.deleteAllDeliveryAddress(loginMember);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }

    @Operation(
        summary = "포인트 충전",
        description = "사용자의 포인트 충전을 위한 API 입니다."
    )
    @PostMapping("/point-charge")
    public ResponseEntity<ApiResponse<ChargePointResponse>> chargePoint(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @RequestBody @Valid ChargePointRequest request
    ) {
        ChargePointResponse chargePointResponse = memberService.chargePoint(request,
            loginMember.getId());
        return ApiResponse.success(ResponseCode.SUCCESS,chargePointResponse);
    }

    @Operation(
        summary = "배송지 목록 조회",
        description = "자신의 배송지 목록을 조회합니다"
    )
    @GetMapping("/delivery-addresses")
    public ResponseEntity<ApiResponse<GetAllAddressResponse>> getDeliveryAddresses(
        @Parameter(hidden = true) @LoginMember Member loginMember
    ){
        GetAllAddressResponse response = memberService.getAllAddress(loginMember.getId());
        return ApiResponse.success(ResponseCode.SUCCESS,response);

    }

    @Operation(
        summary = "회원 삭제",
        description = "회원 삭제를 위한 API입니다"
    )
    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdrawMember(@Parameter(hidden = true) @LoginMember Member loginMember){
        memberService.deleteMember(loginMember.getId());
        return ApiResponse.success(ResponseCode.SUCCESS);

    }
}
