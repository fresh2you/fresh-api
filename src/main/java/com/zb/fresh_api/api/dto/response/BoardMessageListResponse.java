package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.board.BoardMessage;
import com.zb.fresh_api.domain.enums.board.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Objects;

public record BoardMessageListResponse (
    Long boardMessageId,

    MessageType messageType,

    String content,

    boolean isDeleted,

    LocalDateTime createdAt,

    @Schema(description = "내가 쓴 게시글 여부")
    boolean isMine
){
    public static BoardMessageListResponse fromEntity(BoardMessage boardMessage, Long memberId) {
        return new BoardMessageListResponse(boardMessage.getId(),boardMessage.getMessageType(),
            boardMessage.getContent(), boardMessage.getDeletedAt() != null, boardMessage.getCreatedAt(),
            Objects.equals(boardMessage.getMember().getId(), memberId));
    }
}
