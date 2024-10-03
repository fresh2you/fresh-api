package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    @Query("SELECT c FROM ChatRoomMember c WHERE c.chatRoom.chatRoomId = :chatRoomId AND c.memberId = :memberId")
    Optional<ChatRoomMember> findByChatRoomIdAndMemberId(String chatRoomId, Long memberId);

}
