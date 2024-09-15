package com.zb.fresh_api.domain.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;
    private Long chatRoomId;
    private Long memberId;
    private String chatMessageType;
    private String content;
    private LocalDateTime sentAt;

    public ChatMessage(Long chatRoomId, Long memberId, String chatMessageType, String content) {
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.chatMessageType = chatMessageType;
        this.content = content;
        this.sentAt = LocalDateTime.now();
    }
}
