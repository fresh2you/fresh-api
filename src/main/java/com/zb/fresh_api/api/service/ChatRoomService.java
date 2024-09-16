package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomMemberRepository;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    private static final int MAX_PARTICIPANTS = 10;  // 채팅방 최대 인원

    // 채팅방 생성 (1:1 또는 1:10)
    public ChatRoom createChatRoom(Long productId, Long buyerId, Long sellerId, boolean isOneToOne) {
        if (isOneToOne) {
            // 1:1 채팅방 생성
            return createOneToOneChatRoom(productId, buyerId, sellerId);
        } else {
            // 1:10 채팅방 생성
            return createOneToManyChatRoom(productId, sellerId);
        }
    }

    // 1:1 채팅방 생성
    public ChatRoom createOneToOneChatRoom(Long productId, Long buyerId, Long sellerId) {
        // 1:1 채팅방이 이미 있는지 확인
        Optional<ChatRoom> existingRoom = chatRoomRepository.findBySellerIdAndBuyerIdAndProductId(sellerId, buyerId, productId);
        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        // 새 채팅방 생성
        ChatRoom chatRoom = new ChatRoom(productId, null, sellerId, buyerId);
        chatRoom = chatRoomRepository.save(chatRoom);

        // 채팅방에 판매자와 구매자 자동 추가 (자동 승인)
        addParticipant(chatRoom.getId(), sellerId, true, true);
        addParticipant(chatRoom.getId(), buyerId, false, true);

        return chatRoom;
    }

    // 1:10 채팅방 생성
    public ChatRoom createOneToManyChatRoom(Long productId, Long sellerId) {
        // 판매자가 1:10 채팅방을 엶
        ChatRoom chatRoom = new ChatRoom(productId, null, sellerId, null);
        return chatRoomRepository.save(chatRoom);
    }

    // 채팅방 조회
    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + chatRoomId));
    }

    // 마지막 메시지 업데이트
    public void updateLastMessage(Long chatRoomId, String lastMessage) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + chatRoomId));

        chatRoom.setLastMessage(lastMessage);
        chatRoom.setLastMessageSentAt(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
    }

    // 참여자 추가
    public void addParticipant(Long chatRoomId, Long memberId, boolean isSeller, boolean isApproved) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + chatRoomId));

        long participantCount = chatRoomMemberRepository.countByChatRoomId(chatRoomId);

        if (participantCount >= MAX_PARTICIPANTS) {
            throw new IllegalStateException("채팅방의 최대 인원 수를 초과할 수 없습니다.");
        }

        // 판매자 또는 승인된 참여자 추가
        chatRoomMemberRepository.save(new ChatRoomMember(chatRoomId, memberId, isSeller, isApproved));
    }

    // 구매자의 채팅방 참여 요청 승인
    public void approveParticipant(Long chatRoomId, Long memberId) {
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId);
        if (chatRoomMember == null || chatRoomMember.isApproved()) {
            throw new IllegalArgumentException("해당 참여자가 없거나 이미 승인되었습니다.");
        }
        chatRoomMember.setApproved(true);
        chatRoomMemberRepository.save(chatRoomMember);
    }

    // 채팅방 참여 요청 목록 조회 (1:10)
    public List<ChatRoomMember> getPendingRequests(Long chatRoomId) {
        return chatRoomMemberRepository.findByChatRoomId(chatRoomId).stream()
                .filter(member -> !member.isApproved() && !member.isSeller())
                .toList();
    }
}
