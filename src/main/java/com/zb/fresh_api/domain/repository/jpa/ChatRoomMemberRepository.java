package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    ChatRoomMember findByChatRoom_ChatRoomIdAndMemberId(String chatRoomId, Long memberId);  // chatRoomId로 수정
}
