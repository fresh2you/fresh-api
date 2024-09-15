package com.zb.fresh_api.api.websocket;

import com.zb.fresh_api.api.dto.request.ChatMessageRequest;
import com.zb.fresh_api.api.dto.response.ChatMessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChatWebSocketHandlerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatWebSocketHandler chatWebSocketHandler;

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;  // Mock for SimpMessageHeaderAccessor to get senderId from session

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Initialize Mockito
    }

    @Test
    @DisplayName("메시지 전송 성공")
    void sendMessage_success() {
        Long senderId = 1L;
        Long chatRoomId = 100L;
        ChatMessageRequest request = new ChatMessageRequest("Test message", chatRoomId);

        // Actual message handling logic
        ChatMessageResponse response = chatWebSocketHandler.send(request);  // Only passing request

        // Assert the values in response
        assertEquals(senderId.toString(), response.senderId());  // senderId는 String일 가능성이 높음
        assertEquals("Test message", response.message());
        assertEquals(chatRoomId, request.chatRoomId());
    }

    @Test
    @DisplayName("개인 메시지 전송 성공")
    void sendPrivateMessage_success() {
        String sessionId = "session123";
        ChatMessageResponse response = new ChatMessageResponse(UUID.randomUUID().toString(), "user1", "Private message", LocalDateTime.now().toString());

        chatWebSocketHandler.sendPrivateMessage(sessionId, response);

        verify(messagingTemplate, times(1))
                .convertAndSendToUser(eq(sessionId), eq("/topic/private-messages"), eq(response));
    }
}
