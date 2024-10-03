package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatRoomMemberWriter {

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public void save(ChatRoomMember member) {
        chatRoomMemberRepository.save(member);
    }

    public void delete(ChatRoomMember member) {
        chatRoomMemberRepository.delete(member);
    }
}
