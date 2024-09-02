package com.zb.fresh_api.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
    TERMS_NOT_FOUND("0404"),
    SUCCESS("0200");
    private final String code;

}
