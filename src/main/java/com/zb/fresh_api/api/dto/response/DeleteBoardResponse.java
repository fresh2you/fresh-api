package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "게시판 삭제 응답")
public record DeleteBoardResponse(
    @Schema(description = "삭제한 게시판 ID")
    Long boardId,

    @Schema(description = "삭제 시간")
    LocalDateTime deletedAt
) {}
