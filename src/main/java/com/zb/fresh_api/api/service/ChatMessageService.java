package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatMessage;
import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.repository.jpa.ChatMessageRepository;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 메시지 저장 로직
    public ChatMessage saveMessage(Long chatRoomId, Long memberId, String messageType, String content) {
        ChatMessage chatMessage = new ChatMessage(chatRoomId, memberId, messageType, content);
        chatMessageRepository.save(chatMessage);
        updateLastMessageInChatRoom(chatRoomId, content);
        return chatMessage;
    }

    // 채팅방에 마지막 메시지 업데이트
    private void updateLastMessageInChatRoom(Long chatRoomId, String lastMessage) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + chatRoomId));
        chatRoom.setLastMessage(lastMessage);
        chatRoom.setLastMessageSentAt(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
    }
}
