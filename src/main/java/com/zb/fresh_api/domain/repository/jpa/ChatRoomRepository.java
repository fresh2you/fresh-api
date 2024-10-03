package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import org.springframework.data.repository.CrudRepository;

public interface ChatRoomRepository extends CrudRepository<ChatRoom, Long> {
//    Optional<ChatRoom> findBySellerIdAndBuyerIdAndProductId(Long sellerId, Long buyerId, Long productId);
}
