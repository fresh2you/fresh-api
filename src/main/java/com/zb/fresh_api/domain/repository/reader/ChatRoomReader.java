package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChatRoomReader {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<ChatRoom> findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId);
    }

    public Optional<ChatRoom> findBySellerIdAndBuyerIdAndProductId(Long sellerId, Long buyerId, Long productId) {
        return chatRoomRepository.findBySellerIdAndBuyerIdAndProductId(sellerId, buyerId, productId);
    }
}
