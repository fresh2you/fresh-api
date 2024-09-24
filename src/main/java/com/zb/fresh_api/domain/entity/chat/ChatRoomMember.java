package com.zb.fresh_api.domain.entity.chat;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ChatRoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private Long memberId;
    private boolean isSeller;
    private boolean isApproved;

    public ChatRoomMember(ChatRoom chatRoom, Long memberId, boolean isSeller, boolean isApproved) {
        this.chatRoom = chatRoom;
        this.memberId = memberId;
        this.isSeller = isSeller;
        this.isApproved = isApproved;
    }

    public ChatRoomMember approve() {
        return new ChatRoomMember(this.chatRoom, this.memberId, this.isSeller, true);
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public boolean isApproved() {
        return this.isApproved;
    }
}
