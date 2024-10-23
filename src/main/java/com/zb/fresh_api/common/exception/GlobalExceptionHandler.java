package com.zb.fresh_api.common.exception;

import com.zb.fresh_api.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatusCode status, final WebRequest request) {
        final String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        final ResponseCode responseCode = ResponseCode.METHOD_ARG_NOT_VALID;
        log.error("MethodArgumentNotValid Exception : {} ", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, responseCode.getCode(), responseCode.getMessage(), message));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<String>> handle(final CustomException e) {
        log.error("Custom Exception : {} ", e.getMessage());
        return ApiResponse.fail(e.getCommonResponseCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handle(final RuntimeException e) {
        log.error("RuntimeException : {}", e.getMessage());
        return ApiResponse.fail(ResponseCode.COMMON_INVALID_PARAM, e.getMessage());
    }

}
