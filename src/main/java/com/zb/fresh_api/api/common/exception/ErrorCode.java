package com.zb.fresh_api.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    // 닉네임 관련
    NICKNAME_ALREADY_IN_USE("이미 사용중인 닉네임입니다", HttpStatus.CONFLICT),
    PARAM_NICKNAME_NOT_VALID("입력한 닉네임이 잘못되었습니다", HttpStatus.BAD_REQUEST),
    
    // 약관 관련
    TERMS_NOT_FOUND("약관 정보가 존재하지 않습니다", HttpStatus.NOT_FOUND);

    private final String description;
    private final HttpStatus httpStatus;
}
