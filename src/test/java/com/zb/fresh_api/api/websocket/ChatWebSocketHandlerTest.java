package com.zb.fresh_api.api.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.*;

class ChatWebSocketHandlerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ChatWebSocketHandler chatWebSocketHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendPrivateMessage() {
        // 테스트에 사용할 ChatMessageResponse 객체 생성
        ChatMessageResponse chatMessageResponse = new ChatMessageResponse(
                "msg1", "sender1", "Hello WebSocket", "timestamp");

        // 실제 sendPrivateMessage에서 사용하고 있는 경로로 수정
        String sessionId = "session123";
        String destination = "/topic/private-messages";

        // 실제 메서드 호출
        chatWebSocketHandler.sendPrivateMessage(sessionId, chatMessageResponse);

        // convertAndSendToUser가 실제 호출된 경로로 호출되었는지 검증
        verify(messagingTemplate, times(1)).convertAndSendToUser(eq(sessionId), eq(destination), eq(chatMessageResponse));
    }

    @Test
    void testSend() {
        // 테스트에 사용할 ChatMessageRequest 객체 생성
        ChatMessageRequest chatMessageRequest = new ChatMessageRequest(
                "Hello WebSocket", 12345L, "sender1", "receiver1");

        // 실제 메서드 호출
        chatWebSocketHandler.send(chatMessageRequest);

        // ChatMessageResponse 객체 생성
        ChatMessageResponse expectedResponse = new ChatMessageResponse(
                "msg1", chatMessageRequest.senderId(), chatMessageRequest.message(), "timestamp");

        // 목적지 경로 설정
        String destination = "/topic/chatroom/" + chatMessageRequest.chatRoomId();

        // convertAndSend가 호출되었는지 검증
        verify(messagingTemplate, times(1)).convertAndSend(eq(destination), any(ChatMessageResponse.class));
    }

}
