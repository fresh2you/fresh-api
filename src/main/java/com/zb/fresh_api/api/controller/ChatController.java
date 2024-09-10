package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.dto.request.ChatMessageRequest;
import com.zb.fresh_api.api.dto.response.ChatMessageResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class ChatController {

    // 클라이언트가 /app/chat/sendMessage로 메시지를 보내면 처리하는 메소드
    @MessageMapping("/chat/sendMessage")
    @SendTo("/topic/chat/{chatRoomId}")  // 채팅방에 해당하는 topic에 메시지를 보냄
    public ChatMessageResponse sendMessage(@Payload ChatMessageRequest messageRequest,
                                           SimpMessageHeaderAccessor headerAccessor) {
        // SimpMessageHeaderAccessor에서 senderId를 가져옴 (세션에 저장된 사용자 ID)
        String senderId = (String) headerAccessor.getSessionAttributes().get("senderId");

        // 메시지 전송 시 새로운 ChatMessageResponse 생성
        return new ChatMessageResponse(
                UUID.randomUUID().toString(),  // 메시지 ID를 UUID로 생성
                senderId,  // 세션에서 가져온 발신자 ID
                messageRequest.message(),  // 요청으로 받은 메시지 내용
                LocalDateTime.now().toString()  // 메시지가 전송된 시간을 현재 시간으로 설정
        );
    }
}
