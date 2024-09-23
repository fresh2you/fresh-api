package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "게시판 생성 응답")
public record AddBoardResponse(
    Long boardId,

    String title,

    LocalDateTime createdAt
) {}
