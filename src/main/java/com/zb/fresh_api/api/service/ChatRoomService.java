package com.zb.fresh_api.api.service;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatRoomService {

    // 채팅방 정보를 저장할 Map (채팅방 ID -> 참여자 정보)
    private final Map<Long, ChatRoom> chatRooms = new ConcurrentHashMap<>();

    public ChatRoom createChatRoom(Long sellerId, Long buyerId) {
        Long chatRoomId = generateChatRoomId(sellerId, buyerId);
        ChatRoom chatRoom = new ChatRoom(chatRoomId, sellerId, buyerId);
        chatRooms.put(chatRoomId, chatRoom);
        return chatRoom;
    }

    private Long generateChatRoomId(Long sellerId, Long buyerId) {
        return sellerId + buyerId;
    }

    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRooms.get(chatRoomId);
    }

    @Getter
    class ChatRoom {
        private final Long chatRoomId;
        private final Long sellerId;
        private final Long buyerId;

        public ChatRoom(Long chatRoomId, Long sellerId, Long buyerId) {
            this.chatRoomId = chatRoomId;
            this.sellerId = sellerId;
            this.buyerId = buyerId;
        }
    }
}
