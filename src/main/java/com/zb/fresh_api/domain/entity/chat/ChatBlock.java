package com.zb.fresh_api.domain.entity.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED COMMENT 'Primary Key'")
    private Long id;

    @Column(name = "chat_blocker_id", nullable = false, columnDefinition = "BIGINT COMMENT '차단한 사용자 ID'")
    private Long chatBlockerId;

    @Column(name = "chat_blocked_id", nullable = false, columnDefinition = "BIGINT COMMENT '차단당한 사용자 ID'")
    private Long chatBlockedId;

    public static ChatBlock create(Long chatBlockerId, Long chatBlockedId) {
        return new ChatBlock(chatBlockerId, chatBlockedId);
    }

    private ChatBlock(Long chatBlockerId, Long chatBlockedId) {
        this.chatBlockerId = chatBlockerId;
        this.chatBlockedId = chatBlockedId;
    }
}
