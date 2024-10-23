package com.zb.fresh_api.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final CommonResponseCode commonResponseCode;

    public CustomException(CommonResponseCode responseCode) {
        super(responseCode.getMessage());
        this.commonResponseCode = responseCode;
    }

    public CustomException(String message, CommonResponseCode responseCode) {
        super(message);
        this.commonResponseCode = responseCode;
    }

}
