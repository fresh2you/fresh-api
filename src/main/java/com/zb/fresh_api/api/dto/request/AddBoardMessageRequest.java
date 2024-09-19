package com.zb.fresh_api.api.dto.request;

import com.zb.fresh_api.domain.enums.board.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "게시글 추가 요청")
public record AddBoardMessageRequest(

    @Schema(description = "게시글 타입",defaultValue = "TEXT")
    @NotNull MessageType messageType,

    @Schema(description = "게시글 내용")
    @NotBlank String content

) {

}
