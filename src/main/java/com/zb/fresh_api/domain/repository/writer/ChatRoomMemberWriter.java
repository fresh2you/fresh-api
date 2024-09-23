package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.repository.jpa.ChatRoomMemberRepository;
import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChatRoomMemberWriter {

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public Optional<ChatRoomMember> findByChatRoom_ChatRoomIdAndMemberId(String chatRoomId, Long memberId) {
        return chatRoomMemberRepository.findByChatRoom_ChatRoomIdAndMemberId(chatRoomId, memberId);
    }

    public void save(ChatRoomMember member) {
        chatRoomMemberRepository.save(member);
    }

    public void delete(ChatRoomMember member) {
        chatRoomMemberRepository.delete(member);
    }
}
