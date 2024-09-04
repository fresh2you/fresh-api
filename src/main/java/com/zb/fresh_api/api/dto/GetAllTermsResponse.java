package com.zb.fresh_api.api.dto;

import com.zb.fresh_api.domain.entity.terms.Terms;
import lombok.Builder;

import java.util.List;

@Builder
public record GetAllTermsResponse(
        List<TermsDto> termsList) {

    public static GetAllTermsResponse fromDto(List<Terms> dto) {
        return new GetAllTermsResponse(dto.stream()
                .map(TermsDto::fromEntity)
                .toList());
    }
}
