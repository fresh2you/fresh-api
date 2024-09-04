package com.zb.fresh_api.api.dto;

import lombok.Builder;

@Builder
public record GetTermsByIdResponse (
    String title,
    String content,
    boolean isRequired
){
    public static GetTermsByIdResponse fromDto(TermsDto dto){
        return GetTermsByIdResponse.builder()
            .title(dto.title())
            .content(dto.content())
            .isRequired(dto.isRequired())
            .build();
    }
}
