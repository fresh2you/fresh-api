package com.zb.fresh_api.api.dto;

import com.zb.fresh_api.domain.entity.terms.Terms;
import lombok.Builder;

@Builder
public record TermsDto(
    Long termsId,
    String title,
    boolean isRequired
) {
    public static TermsDto from(Terms terms) {
        return TermsDto.builder()
            .termsId(terms.getId())
            .title(terms.getTitle())
            .isRequired(terms.isRequired())
            .build();
    }
}
