package com.zb.fresh_api.domain.entity.product;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @JoinColumn(name = "name", nullable = false, columnDefinition = "varchar(20)  comment '상품명'")
    private String name;

    @JoinColumn(name = "description", nullable = false, columnDefinition = "text  comment '상품설명'")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) comment '가격'")
    private BigDecimal price;

    @Builder.Default
    @Column(name = "created_at", updatable = false, columnDefinition = "datetime comment '스냅샷 생성 일시'")
    private LocalDateTime createdAt = LocalDateTime.now();

    public static ProductSnapshot create(Product product) {
        return ProductSnapshot.builder()
                .product(product)
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

}
