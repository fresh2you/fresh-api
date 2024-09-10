package com.zb.fresh_api.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatRoomServiceTest {

    private ChatRoomService chatRoomService;

    @BeforeEach
    public void setup() {
        chatRoomService = new ChatRoomService();
    }

    @Test
    @DisplayName("채팅방 생성 성공")
    void createChatRoom_success() {
        Long sellerId = 1L;
        Long buyerId = 2L;

        ChatRoomService.ChatRoom chatRoom = chatRoomService.createChatRoom(sellerId, buyerId);

        assertNotNull(chatRoom);
        assertEquals(sellerId, chatRoom.getSellerId());
        assertEquals(buyerId, chatRoom.getBuyerId());
    }

    @Test
    @DisplayName("채팅방 조회 성공")
    void getChatRoom_success() {
        Long sellerId = 1L;
        Long buyerId = 2L;
        ChatRoomService.ChatRoom chatRoom = chatRoomService.createChatRoom(sellerId, buyerId);

        ChatRoomService.ChatRoom fetchedRoom = chatRoomService.getChatRoom(chatRoom.getChatRoomId());

        assertEquals(chatRoom.getChatRoomId(), fetchedRoom.getChatRoomId());
    }
}
