package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.board.BoardMessage;
import java.util.List;

public record GetBoardMessagesResponse(
    List<BoardMessageListResponse> boardMessageList
) {

    public static GetBoardMessagesResponse fromEntities(List<BoardMessage> boardMessageList,
        Long memberId) {
        return new GetBoardMessagesResponse(
            boardMessageList.stream().map(x -> BoardMessageListResponse.fromEntity(x, memberId))
                .toList());
    }
}
