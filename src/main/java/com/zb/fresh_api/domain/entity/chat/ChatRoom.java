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

    private String sellerName;
    private String buyerName;
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
     * 1:1 채팅방 생성 (SellerName과 BuyerName을 조합하여 ChatRoomId 생성)
     */
    public static ChatRoom createOneToOne(String sellerName, String buyerName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.chatRoomId = generateChatRoomId(sellerName, buyerName); // 닉네임 조합으로 ID 생성
        chatRoom.sellerName = sellerName;
        chatRoom.buyerName = buyerName;
        chatRoom.maxParticipants = 2;  // 1:1 채팅방이므로 최대 인원을 2로 설정
        return chatRoom;
    }

    /**
     * 1:10 그룹 채팅방 생성 (SellerName과 현재 시간을 조합하여 ChatRoomId 생성)
     */
    public static ChatRoom createOneToMany(String sellerName, Long productId, Long categoryId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.chatRoomId = generateChatRoomIdForGroup(sellerName); // 판매자 닉네임 기반 ID 생성
        chatRoom.sellerName = sellerName;
        chatRoom.productId = productId;
        chatRoom.categoryId = categoryId;
        chatRoom.maxParticipants = 10;  // 1:10 채팅방이므로 최대 인원을 10으로 설정
        return chatRoom;
    }

    /**
     * 1:1 채팅방 ID 생성 메서드 (SellerName + BuyerName)
     */
    private static String generateChatRoomId(String sellerName, String buyerName) {
        return sellerName + "_" + buyerName;
    }

    /**
     * 1:10 그룹 채팅방 ID 생성 메서드 (SellerName + 현재 시스템 시간)
     */
    private static String generateChatRoomIdForGroup(String sellerName) {
        return sellerName + "_" + System.currentTimeMillis();
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
