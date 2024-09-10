package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "채팅 메시지 전송 요청")
public record ChatMessageRequest(
        @NotBlank(message = "메시지는 비어 있을 수 없습니다.")
        @Schema(description = "메시지 내용")
        String message
) {
}
