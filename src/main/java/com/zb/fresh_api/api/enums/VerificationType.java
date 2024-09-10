package com.zb.fresh_api.api.enums;

import lombok.Getter;

@Getter
public enum VerificationType {
    PHONE("phone:"),
    EMAIL("email");

    private final String prefix;
    VerificationType(String prefix) {
        this.prefix = prefix;
    }
}
