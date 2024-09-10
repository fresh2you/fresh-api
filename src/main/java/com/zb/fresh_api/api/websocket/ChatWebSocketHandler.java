package com.zb.fresh_api.api.websocket;

import com.zb.fresh_api.api.dto.request.ChatMessageRequest;
import com.zb.fresh_api.api.dto.response.ChatMessageResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessageResponse send(ChatMessageRequest messageRequest) {
        return new ChatMessageResponse(
                "1",
                "user1",
                messageRequest.message(),
                String.valueOf(System.currentTimeMillis())
        );
    }

    public void sendPrivateMessage(String sessionId, ChatMessageResponse response) {
        messagingTemplate.convertAndSendToUser(sessionId, "/topic/private-messages", response);
    }
}
