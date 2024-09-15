package com.zb.fresh_api.domain.entity.chat;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatRoomId;
    private Long memberId;
    private boolean isSeller;
    private boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "chatRoomId", insertable = false, updatable = false)
    private ChatRoom chatRoom;

    public ChatRoomMember(Long chatRoomId, Long memberId, boolean isSeller, boolean isApproved) {
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.isSeller = isSeller;
        this.isApproved = isApproved;
    }
}
