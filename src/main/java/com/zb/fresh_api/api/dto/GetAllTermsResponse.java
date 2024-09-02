package com.zb.fresh_api.api.dto;

import lombok.Builder;

@Builder
public record GetAllTermsResponse
    (
        Long termsId,
        String title,
        boolean isRequired
    ) {

    public static GetAllTermsResponse fromDto(TermsDto dto) {
        return GetAllTermsResponse.builder()
            .termsId(dto.termsId())
            .title(dto.title())
            .isRequired(dto.isRequired())
            .build();
    }
}
