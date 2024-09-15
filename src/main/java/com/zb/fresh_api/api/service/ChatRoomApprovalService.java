package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import com.zb.fresh_api.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomApprovalService {

    private final ChatRoomRepository chatRoomRepository;

    // 구매자의 채팅방 참여 요청 처리
    public void requestJoinChatRoom(Long chatRoomId, Long buyerId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다"));

        // 채팅방의 최대 참여자 수를 초과했는지 확인
        if (chatRoom.getMaxParticipants() <= chatRoom.getParticipants().size()) {
            throw new RuntimeException("채팅방이 가득 찼습니다");
        }

        // 참여 요청을 대기 중인 구매자 목록에 추가
        chatRoom.getPendingBuyers().add(buyerId);
        chatRoomRepository.save(chatRoom);
    }

    // 판매자가 구매자의 참여 요청을 승인
    public void approveBuyer(Long chatRoomId, Long sellerId, Long buyerId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다"));

        // 판매자 확인
        if (!chatRoom.getSellerId().equals(sellerId)) {
            throw new UnauthorizedException("오직 판매자만 구매자를 승인할 수 있습니다");
        }

        // 대기 중인 구매자 목록에서 제거하고 참여자 목록에 추가
        chatRoom.getPendingBuyers().remove(buyerId);
        chatRoom.getParticipants().add(buyerId);
        chatRoomRepository.save(chatRoom);
    }
}
