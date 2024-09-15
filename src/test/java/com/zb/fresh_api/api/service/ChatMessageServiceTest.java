package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatMessage;
import com.zb.fresh_api.domain.repository.jpa.ChatMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ChatMessageServiceTest {

    @InjectMocks
    private ChatMessageService chatMessageService;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("메시지 저장 성공")
    void saveMessage_success() {
        // Given
        Long chatRoomId = 1L;
        Long memberId = 2L;
        String messageType = "text";
        String content = "Hello World";

        ChatMessage chatMessage = new ChatMessage(chatRoomId, memberId, messageType, content);

        // When
        chatMessageService.saveMessage(chatRoomId, memberId, messageType, content);

        // Then
        verify(chatMessageRepository, times(1)).save(chatMessage);
    }
}
