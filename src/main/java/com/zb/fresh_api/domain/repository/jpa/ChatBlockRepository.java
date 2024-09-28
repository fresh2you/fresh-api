package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.chat.ChatBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatBlockRepository extends JpaRepository<ChatBlock, Long> {
    Optional<ChatBlock> findByChatBlockerIdAndChatBlockedId(Long chatBlockerId, Long chatBlockedId);
    boolean existsByChatBlockerIdAndChatBlockedId(Long chatBlockerId, Long chatBlockedId);
}
