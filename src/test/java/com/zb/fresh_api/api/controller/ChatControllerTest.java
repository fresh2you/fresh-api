package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.dto.request.ChatMessageRequest;
import com.zb.fresh_api.api.dto.response.ChatMessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChatControllerTest {

    @InjectMocks
    private ChatController chatController;  // 의존성 주입을 위해 @InjectMocks 사용

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;  // Mock 객체

    @Mock
    private SimpMessagingTemplate messagingTemplate;   // Mock 객체

    @BeforeEach
    public void setup() {
        headerAccessor = mock(SimpMessageHeaderAccessor.class);  // Mock 초기화
        messagingTemplate = mock(SimpMessagingTemplate.class);  // Mock 초기화
        chatController = new ChatController();  // ChatController 초기화
    }

    @Test
    @DisplayName("메시지 전송 성공")
    void sendMessage_success() {
        String senderId = "user1";
        ChatMessageRequest request = new ChatMessageRequest("Hello");

        // Mock 세션 속성 설정
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("senderId", senderId);
        when(headerAccessor.getSessionAttributes()).thenReturn(sessionAttributes);

        // 실제 메시지 처리 검증
        ChatMessageResponse response = chatController.sendMessage(request, headerAccessor);

        // Assertions
        assertEquals(senderId, response.senderId());
        assertEquals("Hello", response.message());
    }
}
