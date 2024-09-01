package com.zb.fresh_api.domain.enums.terms;

import com.zb.fresh_api.domain.enums.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TermsType implements Code {

    SERVICE("TT000", "서비스 이용약관"),
    PRIVACY("TT001", "개인정보 수집 및 이용동의"),
    MARKETING("TT002", "마케팅 수신")
    ;

    private final String code;
    private final String description;
}
