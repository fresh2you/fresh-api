package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySellerIdAndBuyerIdAndProductId(Long sellerId, Long buyerId, Long productId);
}
