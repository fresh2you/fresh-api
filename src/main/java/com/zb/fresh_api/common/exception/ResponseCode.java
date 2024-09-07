package com.zb.fresh_api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    /**
     * Success (0200 ~ 0300)
     */
    SUCCESS("0200", "성공했습니다."),

    /**
     * Common (0600 ~ 0700)
     */
    NOT_FOUND_ENUM_CONSTANT("0600", "열거형 상수값을 찾을 수 없습니다."),
    IS_NULL("0601", "NULL 값이 들어왔습니다."),
    COMMON_INVALID_PARAM("0602", "요청한 값이 올바르지 않습니다."),
    INVALID_AUTHENTICATION("0603", "인증이 올바르지 않습니다."),
    NO_SUCH_METHOD("0604", "메소드를 찾을 수 없습니다."),

    /**
     * Member (1000 ~ 1100)
     */
    NICKNAME_ALREADY_IN_USE("1000", "이미 사용중인 닉네임입니다"),
    EMAIL_ALREADY_IN_USE("1001", "이미 사용중인 이메일입니다"),
    PARAM_EMAIL_NOT_VALID("1002", "입력한 이메일이 잘못되었습니다"),
    PARAM_NICKNAME_NOT_VALID("1003", "입력한 닉네임이 잘못되었습니다"),
    NOT_FOUND_MEMBER("1004", "회원 정보를 찾을 수 없습니다."),

    /**
     * Terms (1100 ~ 1200)
     */
    TERMS_NOT_FOUND("1100", "약관 정보가 존재하지 않습니다"),
    TERMS_MANDATORY_NOT_FOUND("1101", "필수 약관이 포함되지 않았습니다"),
    TERMS_MANDATORY_NOT_AGREED("1102", "필수 약관에 동의하지 않았습니다"),

    /**
     * Authentication (1200 ~ 1300)
     */
    INTERNAL_AUTHENTICATION_SERVICE("1200", "인증 서비스가 존재하지 않습니다."),
    NOT_FOUND_OAUTH_PROVIDER("1201", "PROVIDER를 찾을 수 없습니다."),
    NON_EXPIRED_ACCOUNT("1202", "사용자 계정이 탈퇴되었습니다."),
    NON_LOCKED_ACCOUNT("1203", "사용자 계정이 정지되었습니다."),
    DISABLE_ACCOUNT("1204", "사용자 계정은 비활성화 상태입니다."),
    EXPIRED_CREDENTIAL("1205", "사용자 인증 정보가 만료되었습니다."),

    /**
     * Json Web Token (1300 ~ 1400)
     */
    EXPIRED_JWT_EXCEPTION("1300", "만료된 토큰입니다."),
    UNSUPPORTED_JWT_EXCEPTION("1301", "지원되지 않는 토큰입니다."),
    MALFORMED_JWT_EXCEPTION("1302", "잘못된 형식의 토큰입니다."),
    SIGNATURE_EXCEPTION("1303", "토큰 서명이 올바르지 않습니다."),
    ILLEGAL_ARGUMENT_EXCEPTION("1304", "잘못된 인자가 전달되었습니다."),

    /**
     * SMS (1400 ~ 1500)
     */
    EXCEEDED_CERTIFICATION_ATTEMPS("1400", "30분 내에 인증 요청 횟수를 초과했습니다."),
    CERTIFICATION_NOT_FOUND("1401", "인증 유효시간을 초과했습니다"),
    CERTIFICATION_CODE_NOT_CORRECT("1402", "인증 코드가 일치하지 않습니다"),
    NOT_ENOUGH_BALANCE("1403", "서버 관리자에게 문의하세요")
    ;

    private final String code;
    private final String message;
}
