package com.zb.fresh_api.domain.entity.board;

import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
    name = "board"
)
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '상품 고유 번호'")
    private Product product;

    @Column(name = "title", columnDefinition = "varchar(50) comment '제목'")
    private String title;

    @Column(name = "deleted_at", columnDefinition = "datetime comment '게시판 삭제 시간'")
    private LocalDateTime deletedAt;

    @Column(name = "last_messaged_at", columnDefinition = "datetime comment '마지막 문자 시간'")
    private LocalDateTime lastMessagedAt;

    public static Board create(Member member, Product product, String title){
        return Board.builder()
            .member(member)
            .product(product)
            .title(title)
            .build();
    }

    public void update(String title){
        this.title = title;
    }

    public void delete(){
        this.deletedAt = LocalDateTime.now();
    }
}
