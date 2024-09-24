package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import com.zb.fresh_api.api.service.ChatMessageService;
import com.zb.fresh_api.api.service.ChatRoomService;
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
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Tag(name = "채팅 API", description = "채팅방과 관련된 API.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @Operation(summary = "채팅 메시지 리스트 반환", description = "지정된 채팅방 ID에 따른 메시지 리스트를 반환")
    @GetMapping("/chat/{id}")
    public ResponseEntity<ApiResponse<List<ChatMessageDto>>> getChatMessages(
            @Parameter(description = "채팅방 ID") @PathVariable String id) {
        List<ChatMessageDto> chatMessages = chatMessageService.getChatMessages(Long.parseLong(id));
        return ApiResponse.success(ResponseCode.SUCCESS, chatMessages);
    }

    @Operation(summary = "메시지 송신 및 수신", description = "클라이언트에서 /app/message로 메시지를 전송하면 채팅방 구독자들에게 메시지를 전달")
    @MessageMapping("/message")
    public void receiveMessage(
            @Parameter(description = "전송할 메시지") ChatMessageDto chatMessage,
            @Parameter(description = "채팅방 ID") String chatRoomId,
            SimpMessageHeaderAccessor headerAccessor) {
        log.info("Sending message to topic: /sub/chatroom/" + chatRoomId);
        String encodedMessage = new String(chatMessage.message().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        messagingTemplate.convertAndSend("/sub/chatroom/" + chatRoomId, encodedMessage);
    }

    @Operation(summary = "채팅방 나가기", description = "사용자가 채팅방을 나가면 채팅방 멤버에서 제거되고, 마지막 멤버가 나가면 채팅방 삭제")
    @PostMapping("/chat/{chatRoomId}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveChatRoom(
            @Parameter(description = "채팅방 ID") @PathVariable String chatRoomId,
            @Parameter(description = "사용자 ID") @RequestParam Long memberId) {
        chatRoomService.leaveChatRoom(chatRoomId, memberId);
        return ApiResponse.success(ResponseCode.SUCCESS, null);
    }
}
