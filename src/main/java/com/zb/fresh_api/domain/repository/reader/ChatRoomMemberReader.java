package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomMemberReader {

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public Optional<ChatRoomMember> findByChatRoomIdAndMemberId(String chatRoomId, Long memberId) {
        return chatRoomMemberRepository.findByChatRoom_ChatRoomIdAndMemberId(chatRoomId, memberId);
    }
}
