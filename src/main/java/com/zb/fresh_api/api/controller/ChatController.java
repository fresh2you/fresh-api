package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.dto.request.ChatMessageRequest;
import com.zb.fresh_api.api.dto.response.ChatMessageResponse;
import com.zb.fresh_api.api.service.ChatMessageService;
import com.zb.fresh_api.api.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Tag(
        name = "채팅 API",
        description = "채팅방과 관련된 API."
)
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @Operation(
            summary = "채팅 메시지 전송",
            description = "클라이언트가 /app/chat/sendMessage로 메시지를 보내면 채팅방에 메시지를 전송"
    )
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(
            @Valid @Parameter(description = "전송할 채팅 메시지 요청") ChatMessageRequest messageRequest,
            SimpMessageHeaderAccessor headerAccessor) {

        log.info("Broadcasting message to topic: /topic/chatroom/" + messageRequest.chatRoomId());

        String senderId = messageRequest.senderId();
        String receiverId = messageRequest.receiverId();  // 수신자 ID (1:1 채팅의 경우)

        String encodedMessage = new String(messageRequest.message().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        // ChatMessageResponse 생성
        ChatMessageResponse chatMessageResponse = new ChatMessageResponse(
                UUID.randomUUID().toString(),
                senderId,
                encodedMessage,
                LocalDateTime.now().toString()
        );

        // 1:1 채팅의 경우 특정 사용자에게 전송
        if (receiverId != null && !receiverId.isEmpty()) {
            messagingTemplate.convertAndSendToUser(receiverId, "/queue/private", chatMessageResponse);
        } else {
            // 1:10 채팅방의 모든 참여자에게 전송
            messagingTemplate.convertAndSend("/topic/chatroom/" + messageRequest.chatRoomId(), chatMessageResponse);
        }

        // 메시지 저장
        chatMessageService.saveMessage(
                Long.parseLong(messageRequest.chatRoomId().toString()),
                Long.parseLong(senderId),
                "text",
                encodedMessage
        );
    }
}
