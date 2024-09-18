package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "게시판 수정 응답")
public record UpdateBoardResponse(
    @Schema(description = "게시판 ID")
    Long boardId,

    @Schema(description = "게시판명")
    String title,

    @Schema(description = "게시판 수정 시간")
    LocalDateTime updatedAt
) {}
