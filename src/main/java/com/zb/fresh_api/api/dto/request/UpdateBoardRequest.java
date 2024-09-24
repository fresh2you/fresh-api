package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "게시판 수정 요청")
public record UpdateBoardRequest(
   @Schema(description = "게시판 제목")
   @NotBlank
    String title
) {}
