package com.zb.fresh_api.domain.entity.chat;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private Long categoryId;
    private Long sellerId;
    private Long buyerId;
    private String lastMessage;
    private LocalDateTime lastMessageSentAt;
    private Integer maxParticipants = 10;  // 기본값 10

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomMember> members = new HashSet<>();

    // 생성자
    public ChatRoom(Long productId, Long categoryId, Long sellerId, Long buyerId) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastMessageSentAt(LocalDateTime lastMessageSentAt) {
        this.lastMessageSentAt = lastMessageSentAt;
    }

    public Set<Long> getParticipants() {
        Set<Long> participants = new HashSet<>();
        for (ChatRoomMember member : members) {
            if (member.isApproved()) {
                participants.add(member.getMemberId());
            }
        }
        return participants;
    }

    // 대기 중인 구매자 목록 가져오기
    public Set<Long> getPendingBuyers() {
        Set<Long> pendingBuyers = new HashSet<>();
        for (ChatRoomMember member : members) {
            if (!member.isApproved() && !member.isSeller()) {
                pendingBuyers.add(member.getMemberId());
            }
        }
        return pendingBuyers;
    }
}
