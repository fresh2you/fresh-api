package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.dto.request.ChatMessageRequest;
import com.zb.fresh_api.api.dto.response.ChatMessageResponse;
import com.zb.fresh_api.api.service.ChatMessageService;
import com.zb.fresh_api.api.service.ChatRoomService;
import com.zb.fresh_api.common.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ChatControllerTest {

    @InjectMocks
    private ChatController chatController;

    @Mock
    private ChatMessageService chatMessageService;

    @Mock
    private ChatRoomService chatRoomService;

    @Mock
    private SimpMessageHeaderAccessor headerAccessor;

    @BeforeEach
    void setUp() {
        openMocks(this); // Initialize mocks
    }

    @Test
    @DisplayName("채팅 메시지 전송 성공")
    void sendMessage_success() {
        // Given
        String senderId = "1";
        String message = "Hello World";
        Long chatRoomId = 123L;

        // Update the request to match the constructor with message and chatRoomId
        ChatMessageRequest request = new ChatMessageRequest(message, chatRoomId);

        // Set up mock session attributes
        when(headerAccessor.getSessionAttributes()).thenReturn(Map.of("senderId", senderId));

        // When
        ResponseEntity<ApiResponse<ChatMessageResponse>> response = chatController.sendMessage(request, headerAccessor);

        // Then
        verify(chatMessageService, times(1))
                .saveMessage(chatRoomId, Long.valueOf(senderId), "text", message);

        assertEquals(200, response.getStatusCodeValue());

        // Use correct method to get the response body (assuming getData() is not available)
        ChatMessageResponse responseBody = response.getBody().data(); // Adjust based on actual ApiResponse method
        assertEquals(message, responseBody.message());
        assertEquals(senderId, responseBody.senderId());
    }

    @Test
    @DisplayName("채팅방 생성 성공")
    void createChatRoom_success() {
        // Given
        Long productId = 1L;
        Long categoryId = 2L;
        Long memberId = 3L;
        Long sellerId = 4L;
        Long buyerId = 5L;

        // When
        ResponseEntity<ApiResponse<Void>> response = chatController.createChatRoom(productId, categoryId, memberId, sellerId, buyerId);

        // Then
        verify(chatRoomService, times(1)).createChatRoom(productId, categoryId, memberId, sellerId, buyerId);
        assertEquals(200, response.getStatusCodeValue());
    }
}
