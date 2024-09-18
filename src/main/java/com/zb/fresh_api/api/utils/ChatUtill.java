package com.zb.fresh_api.api.utills;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.fresh_api.api.dto.request.ChatMessageRequest;
import com.zb.fresh_api.api.dto.response.ChatMessageResponse;
import com.zb.fresh_api.domain.entity.chat.ChatMessage;
import org.springframework.web.socket.TextMessage;

public class ChatUtill {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ChatMessageRequest를 JSON 문자열로 변환하여 WebSocket 메시지로 보냄
    public static TextMessage resolveTextMessage(ChatMessageResponse message) {
        try {
            return new TextMessage(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // WebSocket에서 받은 JSON 메시지를 ChatMessageRequest로 변환
    public static ChatMessageRequest resolvePayload(String payload) {
        try {
            return objectMapper.readValue(payload, ChatMessageRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // ChatMessage 엔티티를 ChatMessageResponse로 변환
    public static ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
        return new ChatMessageResponse(
                chatMessage.getChatMessageId().toString(),
                chatMessage.getMemberId().toString(),
                chatMessage.getContent(),
                chatMessage.getSentAt().toString()
        );
    }

    // ChatMessageRequest를 ChatMessage 엔티티로 변환
    public static ChatMessage toChatMessage(ChatMessageRequest request, Long memberId, Long chatRoomId) {
        return new ChatMessage(
                chatRoomId,
                memberId,
                "text",  // 메시지 타입은 기본적으로 text로 설정
                request.message()
        );
    }
}
