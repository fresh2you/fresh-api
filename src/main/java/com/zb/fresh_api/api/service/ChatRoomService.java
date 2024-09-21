package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomMemberRepository;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    // 1:1 채팅방 생성
    public ChatRoom createOneToOneChatRoom(Long productId, Long buyerId, Long sellerId) {
        Optional<ChatRoom> existingRoom = chatRoomRepository.findBySellerIdAndBuyerIdAndProductId(sellerId, buyerId, productId);
        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        ChatRoom chatRoom = ChatRoom.createOneToOne(sellerId, buyerId);
        chatRoomRepository.save(chatRoom);

        addParticipant(chatRoom.getChatRoomId(), sellerId, true, true);  // 판매자 자동 참여
        addParticipant(chatRoom.getChatRoomId(), buyerId, false, true);  // 구매자 자동 참여

        return chatRoom;
    }

    // 1:10 채팅방 생성 (채팅방 생성자의 ID에 숫자를 붙여가며 생성)
    public ChatRoom createOneToManyChatRoom(Long productId, Long sellerId) {
        int sequence = 1;  // 시퀀스를 1로 시작
        String chatRoomId;
        Optional<ChatRoom> existingRoom;

        // 중복되지 않는 채팅방 ID가 나올 때까지 시퀀스를 증가시킴
        do {
            chatRoomId = sellerId + String.valueOf(sequence);
            existingRoom = chatRoomRepository.findById(chatRoomId);
            sequence++;
        } while (existingRoom.isPresent());

        // 새 채팅방 생성, sequence를 Long으로 변환하여 전달
        ChatRoom chatRoom = ChatRoom.createOneToMany(sellerId, (long) (sequence - 1), productId);
        chatRoomRepository.save(chatRoom);

        // 채팅방 생성자 자동 참여
        addParticipant(chatRoom.getChatRoomId(), sellerId, true, true);

        return chatRoom;
    }

    // 채팅방에 참가자 추가
    public void addParticipant(String chatRoomId, Long memberId, boolean isSeller, boolean isApproved) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + chatRoomId));

        if (chatRoom.getMembers().size() >= chatRoom.getMaxParticipants()) {
            throw new IllegalStateException("채팅방의 최대 인원 수를 초과할 수 없습니다.");
        }

        ChatRoomMember member = new ChatRoomMember(chatRoom, memberId, isSeller, isApproved);
        chatRoomMemberRepository.save(member);
    }
}
