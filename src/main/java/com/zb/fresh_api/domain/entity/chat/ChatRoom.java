package com.zb.fresh_api.domain.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @Column(name = "chat_room_id")
    private String chatRoomId;

    private Long sellerId;
    private Long buyerId;
    private Long productId;
    private Long categoryId;
    private String lastMessage;
    private Integer maxParticipants = 10;  // 기본값 10

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomMember> members = new HashSet<>();

    // UUID 자동 생성 로직 추가
    @PrePersist
    public void generateUUID() {
        if (this.chatRoomId == null) {
            this.chatRoomId = UUID.randomUUID().toString();
        }
    }

    /**
     * 1:1 채팅방 생성 (SellerId와 BuyerId를 조합하여 ChatRoomId 생성)
     */
    public static ChatRoom createOneToOne(Long sellerId, Long buyerId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.chatRoomId = generateChatRoomId(sellerId, buyerId);
        chatRoom.sellerId = sellerId;
        chatRoom.buyerId = buyerId;
        chatRoom.maxParticipants = 2;  // 1:1 채팅방이므로 최대 인원을 2로 설정
        return chatRoom;
    }

    /**
     * 1:10 그룹 채팅방 생성 (SellerId와 현재 시간을 조합하여 ChatRoomId 생성)
     */
    public static ChatRoom createOneToMany(Long sellerId, Long productId, Long categoryId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.chatRoomId = generateChatRoomIdForGroup(sellerId);
        chatRoom.sellerId = sellerId;
        chatRoom.productId = productId;
        chatRoom.categoryId = categoryId;
        chatRoom.maxParticipants = 10;  // 1:10 채팅방이므로 최대 인원을 10으로 설정
        return chatRoom;
    }

    /**
     * 1:1 채팅방 ID 생성 메서드 (SellerId + BuyerId)
     */
    private static String generateChatRoomId(Long sellerId, Long buyerId) {
        return sellerId + "_" + buyerId;
    }

    /**
     * 1:10 그룹 채팅방 ID 생성 메서드 (SellerId + 현재 시스템 시간)
     */
    private static String generateChatRoomIdForGroup(Long sellerId) {
        return sellerId + "_" + System.currentTimeMillis();
    }

    /**
     * 채팅방에 참여자를 추가
     */
    public void addParticipant(Long memberId) {
        if (this.members.size() >= maxParticipants) {
            throw new IllegalStateException("참가자 수가 최대 인원을 초과했습니다.");
        }
        this.members.add(new ChatRoomMember(this, memberId, false, false));
    }

    /**
     * 채팅방의 마지막 메시지를 업데이트
     */
    public void updateLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
