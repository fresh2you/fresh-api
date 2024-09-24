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

    private Long ChatblockerId;
    private Long ChatblockedId;

    public ChatBlock(Long ChatBlockerId, Long ChatBlockedId) {
        this.ChatblockerId = ChatBlockerId;
        this.ChatblockedId = ChatBlockedId;
    }
}
