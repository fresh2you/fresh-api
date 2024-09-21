package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.board.Board;
import java.util.List;

public record GetAllBoardResponse(
    List<BoardListResponse> boardList
) {
    public static GetAllBoardResponse fromEntities(List<Board> boards) {
        return new GetAllBoardResponse(boards.stream().map(BoardListResponse::fromEntity).toList());
    }
}
