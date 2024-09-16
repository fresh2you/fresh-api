package com.zb.fresh_api.common.response;

import com.zb.fresh_api.common.exception.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public record ApiResponse<T>(
        @Schema(description = "성공 여부", example = "true")
        boolean success,

        @Schema(description = "코드", example = "0200")
        String code,

        @Schema(description = "응답 메세지", example = "성공했습니다.")
        String message,

        @Schema(description = "응답 결과")
        T data) {

    public static <T> ResponseEntity<ApiResponse<T>> success(ResponseCode responseCode) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, responseCode.getCode(), responseCode.getMessage(), null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(ResponseCode responseCode, T data) {
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>(true, responseCode.getCode(), responseCode.getMessage(), data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(ResponseCode responseCode) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, responseCode.getCode(), responseCode.getMessage(), null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(ResponseCode responseCode, T data) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, responseCode.getCode(), responseCode.getMessage(), data));
    }

    public static ResponseEntity<ApiResponse<String>> error(ResponseCode responseCode, RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, responseCode.getCode(), responseCode.getMessage(), exception.getMessage()));
    }

}
