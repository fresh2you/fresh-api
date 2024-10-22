package com.zb.fresh_api.domain.entity.chat;

import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.enums.chat.ChatRoomStatus;
import com.zb.fresh_api.domain.enums.chat.ChatRoomType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "chat_room"
)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '방 생성자, 회원 고유 번호'")
    private ChatRoomMember owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '연결 상품, 상품 고유 번호'")
    private Product product;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(255) comment '방 이름'")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20) comment '채팅방 상태'")
    private ChatRoomStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "varchar(20) comment '채팅방 타입(개인, 그룹)'")
    private ChatRoomType type;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제 일시'")
    private LocalDateTime deletedAt;

    public void deleteByOwner() {
        this.status = ChatRoomStatus.OWNER_DELETED;
        this.deletedAt = LocalDateTime.now();
    }

}
