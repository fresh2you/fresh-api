package com.zb.fresh_api.common.exception;

import com.zb.fresh_api.api.dto.KakaoOAuthResponse;
import com.zb.fresh_api.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> handle(CustomException e) {
        log.error("Custom Exception : {} ", e.getMessage());
        return ApiResponse.fail(e.getResponseCode());
    }
    public static GlobalExceptionHandler<KakaoOAuthResponse> ofSuccess(KakaoOAuthResponse response, String 카카오_로그인_성공) {
        return GlobalExceptionHandler.<KakaoOAuthResponse>builder().build();
    }
}
