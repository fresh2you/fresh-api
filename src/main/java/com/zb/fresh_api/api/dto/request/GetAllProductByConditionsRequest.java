package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상품 목록 조회 시 사용하는 요청")
public record GetAllProductByConditionsRequest(
    @Schema(description = "카테고리 ID")
    Long categoryId,
    
    @Schema(description = "검색어")
    String keyword,
    
    @Schema(description = "페이지 번호", defaultValue = "0")
    int page,
    
    @Schema(description = "페이지 크기",defaultValue = "20")
    int size
){}
