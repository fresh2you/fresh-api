package com.zb.fresh_api.domain.entity.board;

import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.enums.board.MessageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "board_message"
)
public class BoardMessage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '게시판 고유 번호'")
    private Board board;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", columnDefinition = "varchar(255) comment '메시지 타입'")
    private MessageType messageType;

    @Column(name = "content", columnDefinition = "varchar(255) comment '메시지 내용'")
    private String content;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '게시글 삭제 시간'")
    private LocalDateTime deletedAt;

    public static BoardMessage create(Board board, MessageType messageType, String content){
        return BoardMessage.builder()
            .board(board)
            .messageType(messageType)
            .content(content)
            .build();
    }
}
