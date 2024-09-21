package com.zb.fresh_api.domain.entity.product;

import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.entity.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "product_snapshot"
)
@EntityListeners(AuditingEntityListener.class)
public class ProductSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '상품 고유 번호'")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '카테고리 고유 번호'")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @JoinColumn(name = "name", nullable = false, columnDefinition = "varchar(20)  comment '상품명'")
    private String name;

    @JoinColumn(name = "description", nullable = false, columnDefinition = "text  comment '상품설명'")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) comment '가격'")
    private BigDecimal price;

    @CreatedDate
    @Column(name = "snapshot_date", updatable = false, columnDefinition = "datetime comment '스냅샷 생성 일시'")
    private LocalDateTime snapshotDate;

    public static ProductSnapshot create(Product product) {
        return  ProductSnapshot.builder()
            .product(product)
            .category(product.getCategory())
            .member(product.getMember())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .build();
    }

}
