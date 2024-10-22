package com.zb.fresh_api.domain.entity.chat;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "chat_message"
)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '채팅방 고유 번호'")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private ChatRoomMember sender;

    @Column(name = "message", columnDefinition = "varchar(255) comment '메세지'")
    private String message;

    @Column(name = "created_at", updatable = false, columnDefinition = "datetime comment '발송 일시'")
    private LocalDateTime createdAt;

}
