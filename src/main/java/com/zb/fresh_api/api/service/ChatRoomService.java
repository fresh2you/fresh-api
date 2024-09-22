package com.zb.fresh_api.api.service;

import com.zb.fresh_api.domain.entity.chat.ChatMessage;
import com.zb.fresh_api.domain.entity.chat.ChatRoomMember;
import com.zb.fresh_api.domain.repository.jpa.ChatMessageRepository;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomMemberRepository;
import com.zb.fresh_api.domain.repository.jpa.ChatRoomRepository;
import com.zb.fresh_api.domain.entity.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoom createOneToOneChatRoom(Long productId, Long buyerId, Long sellerId) {
        Optional<ChatRoom> existingRoom = chatRoomRepository.findBySellerIdAndBuyerIdAndProductId(sellerId, buyerId, productId);
        if (existingRoom.isPresent()) {
            return existingRoom.get();
        }

        ChatRoom chatRoom = ChatRoom.createOneToOne(sellerId, buyerId);
        chatRoomRepository.save(chatRoom);

        addParticipant(chatRoom.getChatRoomId(), sellerId, true, true);
        addParticipant(chatRoom.getChatRoomId(), buyerId, false, true);

        return chatRoom;
    }

    public ChatRoom createOneToManyChatRoom(Long productId, Long sellerId) {
        int sequence = 1;
        String chatRoomId;
        Optional<ChatRoom> existingRoom;

        do {
            chatRoomId = sellerId + String.valueOf(sequence);
            existingRoom = chatRoomRepository.findById(chatRoomId);
            sequence++;
        } while (existingRoom.isPresent());

        ChatRoom chatRoom = ChatRoom.createOneToMany(sellerId, (long) (sequence - 1), productId);
        chatRoomRepository.save(chatRoom);

        addParticipant(chatRoom.getChatRoomId(), sellerId, true, true);

        return chatRoom;
    }

    public void addParticipant(String chatRoomId, Long memberId, boolean isSeller, boolean isApproved) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + chatRoomId));

        if (chatRoom.getMembers().size() >= chatRoom.getMaxParticipants()) {
            throw new IllegalStateException("채팅방의 최대 인원 수를 초과할 수 없습니다.");
        }

        ChatRoomMember member = new ChatRoomMember(chatRoom, memberId, isSeller, isApproved);
        chatRoomMemberRepository.save(member);
    }

    public void leaveChatRoom(String chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다: " + chatRoomId));

        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoom_ChatRoomIdAndMemberId(chatRoomId, memberId);

        if (chatRoomMember == null) {
            throw new IllegalArgumentException("참여자가 채팅방에 존재하지 않습니다.");
        }

        chatRoomMemberRepository.delete(chatRoomMember); // 멤버 삭제

        if (chatRoom.getMembers().isEmpty()) {
            chatRoomRepository.delete(chatRoom); // 채팅방에 멤버가 없으면 채팅방 삭제
        }
    }
    public List<ChatMessage> getMessagesByChatRoomId(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }

    public void saveMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }
}
