package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class ChatRoomWriter {

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    public ChatRoom store(ChatRoom chatRoom) {
        return chatRoomJpaRepository.save(chatRoom);
    }
}
