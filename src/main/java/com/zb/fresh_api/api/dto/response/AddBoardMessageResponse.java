package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.board.BoardMessage;
import com.zb.fresh_api.domain.enums.board.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "게시판 생성 응답")
public record AddBoardMessageResponse(
    
    @Schema(description = "게시글 ID")
    Long boardMessageId,

    @Schema(description = "게시글 타입")
    MessageType messageType,
   
    @Schema(description = "게시글 내용")
    String content,
   
    @Schema(description = "게시글 생성 시간")
    LocalDateTime createdAt
) {
    public AddBoardMessageResponse(BoardMessage boardMessage){
        this(boardMessage.getId(),boardMessage.getMessageType(),boardMessage.getContent()
        ,boardMessage.getCreatedAt());
    }
}
