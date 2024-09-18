package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatMessage;
import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.repository.jpa.ChatMessageRepository;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatMessageService chatMessageService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("메시지 저장 성공")
    void saveMessage_success() {
        Long chatRoomId = 1L;
        Long memberId = 1L;
        String messageType = "text";
        String content = "Hello";

        ChatMessage chatMessage = new ChatMessage(chatRoomId, memberId, messageType, content);

        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(new ChatRoom()));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);

        chatMessageService.saveMessage(chatRoomId, memberId, messageType, content);

        verify(chatRoomRepository, times(1)).findById(chatRoomId);
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }
}
