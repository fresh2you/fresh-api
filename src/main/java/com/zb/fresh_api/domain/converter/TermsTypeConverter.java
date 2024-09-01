package com.zb.fresh_api.domain.converter;


import com.zb.fresh_api.domain.enums.terms.TermsType;
import jakarta.persistence.Converter;

@Converter
public class TermsTypeConverter extends CodeConverter<TermsType> {

    public TermsTypeConverter() {
        super(TermsType.class);
    }
}
