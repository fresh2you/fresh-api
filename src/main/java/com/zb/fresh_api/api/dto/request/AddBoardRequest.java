package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "게시판 생성 요청")
public record AddBoardRequest(
    @Schema(description = "게시판 제목")
    @NotBlank
    String title,

    @Schema(description = "상품 ID")
    Long productId
) {}
