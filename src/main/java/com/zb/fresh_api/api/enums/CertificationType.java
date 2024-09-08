package com.zb.fresh_api.api.enums;

import lombok.Getter;

@Getter
public enum CertificationType {
    PHONE("phone:"),
    EMAIL("email");

    private final String prefix;
    CertificationType(String prefix) {
        this.prefix = prefix;
    }
}
