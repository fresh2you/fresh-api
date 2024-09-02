package com.zb.fresh_api.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    TERMS_NOT_FOUND("약관 정보가 존재하지 않습니다", HttpStatus.NOT_FOUND);

    private final String description;
    private final HttpStatus httpStatus;
}
