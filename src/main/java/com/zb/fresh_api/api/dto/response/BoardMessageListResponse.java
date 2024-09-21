package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.board.BoardMessage;
import com.zb.fresh_api.domain.enums.board.MessageType;
import java.time.LocalDateTime;

public record BoardMessageListResponse (
    Long boardMessageId,

    MessageType messageType,

    String content,

    boolean isDeleted,

    LocalDateTime createdAt
){
    public static BoardMessageListResponse fromEntity(BoardMessage boardMessage) {
        return new BoardMessageListResponse(boardMessage.getId(),boardMessage.getMessageType(),
            boardMessage.getContent(), boardMessage.getDeletedAt() != null, boardMessage.getCreatedAt());
    }
}
