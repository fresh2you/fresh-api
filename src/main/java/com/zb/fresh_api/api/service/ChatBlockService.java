package com.zb.fresh_api.api.service;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.chat.ChatBlock;
import com.zb.fresh_api.domain.repository.jpa.ChatBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatBlockService {

    private final ChatBlockRepository chatBlockRepository;

    public void blockUser(Long chatBlockerId, Long chatBlockedId) {
        if (chatBlockRepository.existsByChatBlockerIdAndChatBlockedId(chatBlockerId, chatBlockedId)) {
            throw new CustomException(ResponseCode.ALREADY_BLOCKED);
        }

        ChatBlock chatBlock = ChatBlock.create(chatBlockerId, chatBlockedId);
        chatBlockRepository.save(chatBlock);
    }

    public void unblockUser(Long chatBlockerId, Long chatBlockedId) {
        ChatBlock chatBlock = chatBlockRepository.findByChatBlockerIdAndChatBlockedId(chatBlockerId, chatBlockedId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_BLOCKED));

        chatBlockRepository.delete(chatBlock);
    }

    public boolean isBlocked(Long chatBlockerId, Long chatBlockedId) {
        return chatBlockRepository.existsByChatBlockerIdAndChatBlockedId(chatBlockerId, chatBlockedId);
    }
}
