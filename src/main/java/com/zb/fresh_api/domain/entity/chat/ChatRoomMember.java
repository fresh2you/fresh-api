package com.zb.fresh_api.domain.entity.chat;

import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.chat.ChatRoomMemberType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "chat_room_member"
)
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '채팅방 고유 번호'")
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(20) comment '채팅 참여자 타입'")
    private ChatRoomMemberType type;

    @Builder.Default
    private Long lastReadMessageId = null;

    public void updateLastReadMessageId(Long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }

}
