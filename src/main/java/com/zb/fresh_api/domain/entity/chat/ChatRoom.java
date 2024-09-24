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
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public static ChatRoom createOneToOne(Long sellerId, Long buyerId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.sellerId = sellerId;
        chatRoom.buyerId = buyerId;
        chatRoom.maxParticipants = 2;  // 1:1 채팅방이므로 최대 인원을 2로 설정
        return chatRoom;
    }

    public static ChatRoom createOneToMany(Long sellerId, Long productId, Long categoryId) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.sellerId = sellerId;
        chatRoom.productId = productId;
        chatRoom.categoryId = categoryId;
        chatRoom.maxParticipants = 10;  // 1:10 채팅방이므로 최대 인원을 10으로 설정
        return chatRoom;
    }

    public void addParticipant(Long memberId) {
        if (this.members.size() >= maxParticipants) {
            throw new IllegalStateException("참가자 수가 최대 인원을 초과했습니다.");
        }
        this.members.add(new ChatRoomMember(this, memberId, false, false));
    }

    public void updateLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
