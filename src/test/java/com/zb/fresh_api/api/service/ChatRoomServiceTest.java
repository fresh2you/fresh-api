package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
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

        ChatRoom chatRoom = new ChatRoom(productId, categoryId, memberId, buyerId, sellerId);

        // When
        chatRoomService.createChatRoom(productId, categoryId, memberId, sellerId, buyerId);

        // Then
        verify(chatRoomRepository, times(1)).save(chatRoom);
    }
}
