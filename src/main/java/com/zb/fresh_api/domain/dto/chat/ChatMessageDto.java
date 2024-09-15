package com.zb.fresh_api.domain.chat;

public record ChatMessageDto(
        Long chatRoomId,
        Long memberId,
        MessageType messageType,
        String content
) {
    public enum MessageType {
        TEXT, IMAGE
    }
}

