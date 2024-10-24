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
     * Common (0600 ~ 0800)
     */
    INTERNAL_SERVER_ERROR("0600", "내부 서버 오류가 발생하였습니다."),
    IS_NULL("0601", "NULL 값이 들어왔습니다."),
    COMMON_INVALID_PARAM("0602", "요청한 값이 올바르지 않습니다."),
    INVALID_AUTHENTICATION("0603", "인증이 올바르지 않습니다."),
    NO_SUCH_METHOD("0604", "메소드를 찾을 수 없습니다."),
    NOT_FOUND_ENUM_CONSTANT("0605", "열거형 상수값을 찾을 수 없습니다."),
    S3_UPLOADER_ERROR("0606", "S3 업로드 중 오류가 발생하였습니다."),
    METHOD_ARG_NOT_VALID("0607", "유효성 오류가 발생했습니다."),
    INVALID_FILE_NAME_OR_EXTENSIONS("0608", "파일 이름 또는 확장자가 잘못되었습니다."),
    INVALID_LIST("0609", "리스트가 비어있거나 NULL 입니다."),

    FORBIDDEN("0700", "접근 권한이 없습니다."),
    UNAUTHORIZED("0701", "유효한 인증 자격이 없습니다."),

    /**
     * Member (1000 ~ 1100)
     */
    NICKNAME_ALREADY_IN_USE("1000", "이미 사용중인 닉네임입니다"),
    EMAIL_ALREADY_IN_USE("1001", "이미 사용중인 이메일입니다"),
    PARAM_EMAIL_NOT_VALID("1002", "입력한 이메일이 잘못되었습니다"),
    PARAM_NICKNAME_NOT_VALID("1003", "입력한 닉네임이 잘못되었습니다"),
    NOT_FOUND_MEMBER("1004", "회원 정보를 찾을 수 없습니다."),
    INVALID_PASSWORD_MATCH("1005", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    INVALID_PASSWORD_FORMAT("1006", "비밀번호는 최소 8글자 이상, 최대 16글자 이하로 작성해야 하며, 비밀번호는 영어와 숫자를 혼용해야 하며, 공백은 사용할 수 없습니다."),
    INVALID_PROVIDER("1007", "회원가입 경로를 확인해주세요.(이메일, 소셜)"),
    MEMBER_NOT_SELLER("1008", "사용자가 판매자가 아닙니다"),
    INVALID_PASSWORD("1009","비밀번호가 일치하지 않습니다"),
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
    UNAUTHORIZED_ACCESS_EXCEPTION("1206", "사용자가 판매자가 아닙니다."),

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
    EXCEEDED_VERIFICATION_ATTEMPS("1400", "하루 인증 요청 횟수를 초과했습니다."),
    VERIFICATION_NOT_FOUND("1401", "인증이 존재하지 않거나 인증 유효시간을 초과했습니다"),
    VERIFICATION_CODE_NOT_CORRECT("1402", "인증 코드가 일치하지 않습니다"),
    NOT_ENOUGH_BALANCE("1403", "서버 관리자에게 문의하세요"),
    GOOGLE_SMTP_ERROR("1404", "서버 관리자에게 문의하세요"),
    PHONE_ALREADY_IN_USE("1405", "이미 사용중인 휴대전화 입니다"),
    MEMBER_ALREADY_SELLER("1406", "이미 판매자 인증을 받은 사용자입니다"),
    /**
     * Category (1500 ~ 1600)
     */
    CATEGORY_NOT_FOUND("1500", "카테고리를 찾을 수 없습니다."),

    /**
     * Product (1600 ~ 1700)
     */
    PRODUCT_NOT_FOUND("1600", "상품을 찾을 수 없습니다"),
    NOT_PRODUCT_OWNER("1601", "수정하려는 사용자가 판매자와 일치하지 않습니다."),
    NOT_ENOUGH_QUANTITY("1602", "상품 수량이 충분하지 않습니다."),

    /**
     * ProductLike (1700 ~ 1800)
     */
    PRODUCT_LIKE_NOT_FOUND("1700", "상품에 대한 좋아요가 존재하지 않습니다"),
    PRODUCT_ALREADY_LIKED("1701", "이미 좋아요한 상품입니다"),

    /**
     * Board (1800 ~ 1900)
     */
    BOARD_NOT_FOUND("1800", "게시판이 존재하지 않습니다"),
    BOARD_ALREADY_EXIST("1801", "이미 상품에 대한 게시판이 존재합니다"),
    NOT_BOARD_OWNER("1802", "사용자가 판매자와 일치하지 않습니다." ),
    BOARD_MESSAGE_NOT_FOUND("1803", "게시글이 존재하지 않습니다"),

    /**
     * Delivery (1900 ~ 2000)
     */
    NOT_FOUND_DELIVERY_ADDRESS("2000", "등록된 배송지 정보를 찾을 수 없습니다."),
    EXCEEDED_DELIVERY_ADDRESS_COUNT("2001", "등록할 수 있는 배송지의 개수가 초과되었습니다. (최대 3개)"),

    /**
     * 채팅방 관련 오류 코드 (2000 ~ 2100)
     */
    NOT_FOUND_CHATROOM("2000", "채팅방을 찾을 수 없습니다."),
    MAX_PARTICIPANTS_EXCEEDED("2001", "참가자 수가 최대 인원을 초과했습니다."),
    NOT_FOUND_CHATROOM_MEMBER("2002", "채팅방 멤버를 찾을 수 없습니다."),
    ALREADY_BLOCKED("2003", "이미 차단한 사용자입니다."),
    NOT_BLOCKED("2004", "차단된 사용자가 아닙니다."),
    BLOCKED_BY_USER("2005", "차단된 사용자로 인해 메시지를 보낼 수 없습니다."),
    CHATROOM_ALREADY_EXISTS("2006","채팅방이 이미 존재합니다."),

    /**
     * Point (2100 ~ 2200)
     */
    POINT_NOT_FOUND("2100", "포인트 정보를 찾을 수 없습니다"),
    POINT_NOT_ENOUGH("2101", "포인트가 충분하지 않습니다")

    ;

    private final String code;
    private final String message;
}
