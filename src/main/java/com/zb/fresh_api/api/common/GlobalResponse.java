package com.zb.fresh_api.api.common;

import lombok.Builder;

@Builder
public record GlobalResponse<T> (
    boolean success,
    String code,
    String message,
    T data
){
}
