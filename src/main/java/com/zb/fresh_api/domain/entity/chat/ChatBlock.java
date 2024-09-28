package com.zb.fresh_api.domain.entity.chat;

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
    private Long id;

    private Long chatBlockerId;
    private Long chatBlockedId;

    public static ChatBlock create(Long chatBlockerId, Long chatBlockedId) {
        return new ChatBlock(chatBlockerId, chatBlockedId);
    }

    private ChatBlock(Long chatBlockerId, Long chatBlockedId) {
        this.chatBlockerId = chatBlockerId;
        this.chatBlockedId = chatBlockedId;
    }
}
