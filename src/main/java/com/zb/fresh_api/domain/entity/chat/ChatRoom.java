package com.zb.fresh_api.domain.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @Column(name = "chat_room_id")
    private Long chatRoomId;
    private String sellerName;
    private String buyerName;
    private Long productId;
    private Long categoryId;
    private String lastMessage;
    private Integer maxParticipants = 10;  // 기본값 10

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomMember> members = new HashSet<>();

    // ID가 없는 경우 예외 처리
    @PrePersist
    public void validateId() {
        if (this.chatRoomId == null) {
            throw new IllegalStateException("ChatRoom ID가 설정되지 않았습니다.");
        }
    }

    /**
     * 1:1 채팅방 생성 (SellerName과 BuyerName을 숫자로 변환하여 ChatRoomId 생성)
     */
    public static ChatRoom createOneToOne(String sellerName, String buyerName) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.chatRoomId = ChatRoomIdGenerator.generateSha256ChatRoomId(sellerName, buyerName);  // SHA-256 또는 hashCode 방식 사용
        chatRoom.sellerName = sellerName;
        chatRoom.buyerName = buyerName;
        chatRoom.maxParticipants = 2;  // 1:1 채팅방이므로 최대 인원을 2로 설정
        return chatRoom;
    }

    /**
     * 1:10 그룹 채팅방 생성 (SellerName을 숫자로 변환하여 ChatRoomId 생성)
     */
    public static ChatRoom createOneToMany(String sellerName, Long productId, Long categoryId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.chatRoomId = ChatRoomIdGenerator.generateSha256ChatRoomId(sellerName, "GROUP_CHAT");  // 그룹 ID 생성
        chatRoom.sellerName = sellerName;
        chatRoom.productId = productId;
        chatRoom.categoryId = categoryId;
        chatRoom.maxParticipants = 10;  // 1:10 채팅방이므로 최대 인원을 10으로 설정
        return chatRoom;
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
