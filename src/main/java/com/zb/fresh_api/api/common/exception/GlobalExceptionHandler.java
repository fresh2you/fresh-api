package com.zb.fresh_api.api.common.exception;

import com.zb.fresh_api.api.common.GlobalResponse;
import com.zb.fresh_api.api.common.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<GlobalResponse<?>> handleCustomException(final CustomException e) {

        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(
            GlobalResponse.builder()
                .success(false)
                .code(ResponseCode.TERMS_NOT_FOUND.getCode())
                .message(e.getErrorCode().getDescription())
                .data(null)
                .build()
        );
    }

}
