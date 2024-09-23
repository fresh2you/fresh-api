package com.zb.fresh_api.domain.entity.order;

import com.zb.fresh_api.domain.entity.address.DeliveryAddressSnapshot;
import com.zb.fresh_api.domain.entity.product.ProductSnapshot;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "product_order"
)
public class ProductOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_snapshot_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '상품 스냅샷 고유 번호'")
    private ProductSnapshot productSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_snapshot_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '배송지 스냅샷 고유 번호'")
    private DeliveryAddressSnapshot deliveryAddressSnapshot;

    @Column(name = "created_at", nullable = false, columnDefinition = "varchar(20) comment '상태'")
    private LocalDateTime createdAt;

}
