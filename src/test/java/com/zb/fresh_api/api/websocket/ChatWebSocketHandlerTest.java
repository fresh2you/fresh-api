package com.zb.fresh_api.api.websocket;

import static org.mockito.Mockito.*;

import com.zb.fresh_api.api.dto.request.ChatMessageRequest;
import com.zb.fresh_api.api.dto.response.ChatMessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChatWebSocketHandlerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatWebSocketHandler chatWebSocketHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    @DisplayName("메시지 전송 성공")
    void sendMessage_success() {
        ChatMessageRequest request = new ChatMessageRequest("Test message");

        ChatMessageResponse response = chatWebSocketHandler.send(request);

        assertEquals("user1", response.senderId());
        assertEquals("Test message", response.message());
    }

    @Test
    @DisplayName("개인 메시지 전송 성공")
    void sendPrivateMessage_success() {
        String sessionId = "session123";
        ChatMessageResponse response = new ChatMessageResponse("1", "user1", "Private message", "timestamp");

        chatWebSocketHandler.sendPrivateMessage(sessionId, response);

        verify(messagingTemplate, times(1))
                .convertAndSendToUser(eq(sessionId), eq("/topic/private-messages"), eq(response));
    }
}
