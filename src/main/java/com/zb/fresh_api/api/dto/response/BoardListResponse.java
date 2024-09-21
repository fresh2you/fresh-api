package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.board.Board;
import java.time.LocalDateTime;

public record BoardListResponse(
    Long boardId,

    String title,

    LocalDateTime lastMessagedAt
) {
    public static BoardListResponse fromEntity(Board board) {
        return new BoardListResponse(board.getId(), board.getTitle(), board.getLastMessagedAt());
    }
}
