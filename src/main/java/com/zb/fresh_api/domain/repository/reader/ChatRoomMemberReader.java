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

    // 채팅방 ID와 멤버 ID로 채팅방 멤버 찾기 (Optional 반환)
    public Optional<ChatRoomMember> findByChatRoomIdAndMemberId(String chatRoomId, Long memberId) {
        return chatRoomMemberRepository.findByChatRoom_ChatRoomIdAndMemberId(chatRoomId, memberId);
    }
}
