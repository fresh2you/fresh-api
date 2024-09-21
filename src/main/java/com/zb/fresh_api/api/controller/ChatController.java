package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.domain.dto.chat.ChatMessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(
        name = "채팅 API",
        description = "채팅방과 관련된 API."
)
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @Operation(
            summary = "채팅 메시지 리스트 반환",
            description = "지정된 채팅방 ID에 따른 메시지 리스트를 반환"
    )
    @GetMapping("/chat/{id}")
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(
            @Parameter(description = "채팅방 ID") @PathVariable Long id) {
        // 임시로 리스트 형식으로 구현, 실제론 DB 접근 필요
        ChatMessageDto test = new ChatMessageDto(1L, "test", "test");
        return ResponseEntity.ok().body(List.of(test));
    }

    @Operation(
            summary = "메시지 송신 및 수신",
            description = "클라이언트에서 /app/message로 메시지를 전송하면 채팅방 구독자들에게 메시지를 전달"
    )
    @MessageMapping("/message")
    public void receiveMessage(
            @Parameter(description = "전송할 메시지") ChatMessageDto chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {

        log.info("Sending message to topic: /sub/chatroom/1");

        // 메시지 인코딩
        String encodedMessage = new String(chatMessage.message().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        // 메시지를 해당 채팅방 구독자들에게 전송
        messagingTemplate.convertAndSend("/sub/chatroom/1", encodedMessage);
    }
}
