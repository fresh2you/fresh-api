package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    Optional<ChatRoomMember> findByChatRoom_ChatRoomIdAndMemberId(String chatRoomId, Long memberId);
}
