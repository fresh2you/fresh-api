package com.zb.fresh_api.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0200", "응답 성공");
    private final String code;
    private final String message;

}
