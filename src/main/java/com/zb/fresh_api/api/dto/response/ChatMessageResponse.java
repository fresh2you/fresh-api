package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅 메시지 응답")
public record ChatMessageResponse(
        @Schema(description = "메시지 ID")
        String messageId,

        @Schema(description = "송신자 ID")
        String senderId,

        @Schema(description = "수신된 메시지 내용")
        String message,

        @Schema(description = "메시지 수신 시간")
        String timestamp
) {
}
