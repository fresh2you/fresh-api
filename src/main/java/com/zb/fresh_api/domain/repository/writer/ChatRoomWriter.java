package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomWriter {

    private final ChatRoomRepository chatRoomRepository;

    public void save(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
    }

    public void delete(ChatRoom chatRoom) {
        chatRoomRepository.delete(chatRoom);
    }
}
