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

    // 사용자를 차단
    public void blockUser(Long chatBlockerId, Long chatBlockedId) {
        if (chatBlockRepository.existsByChatBlockerIdAndChatBlockedId(chatBlockerId, chatBlockedId)) {
            throw new CustomException(ResponseCode.ALREADY_BLOCKED);
        }

        ChatBlock chatBlock = new ChatBlock(chatBlockerId, chatBlockedId);
        chatBlockRepository.save(chatBlock);
    }

    // 사용자의 차단 해제
    public void unblockUser(Long chatBlockerId, Long chatBlockedId) {
        ChatBlock chatBlock = chatBlockRepository.findByChatBlockerIdAndChatBlockedId(chatBlockerId, chatBlockedId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_BLOCKED));

        chatBlockRepository.delete(chatBlock);
    }

    // 차단 여부 확인
    public boolean isBlocked(Long chatBlockerId, Long chatBlockedId) {
        return chatBlockRepository.existsByChatBlockerIdAndChatBlockedId(chatBlockerId, chatBlockedId);
    }
}
