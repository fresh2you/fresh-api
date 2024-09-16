package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "제품 등록 응답")
public record AddProductResponse (
    @Schema(description = "제품 ID")
    Long id,
    
    @Schema(description = "제품 이름")
    String name,
    
    @Schema(description = "제품 등록 시간")
    LocalDateTime createdAt
){}
