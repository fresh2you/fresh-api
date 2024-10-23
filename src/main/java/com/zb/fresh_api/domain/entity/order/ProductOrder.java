package com.zb.fresh_api.domain.entity.order;

import com.zb.fresh_api.domain.entity.address.DeliveryAddressSnapshot;
import com.zb.fresh_api.domain.entity.product.ProductSnapshot;
import jakarta.persistence.*;
import lombok.*;
import com.zb.fresh_api.domain.enums.order.ProductOrderStatus;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "product_order"
)
@EntityListeners(AuditingEntityListener.class)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "product_order_status", nullable = false, columnDefinition = "varchar(20) comment '주문 상태'")
    private ProductOrderStatus productOrderStatus;

    @Column(name = "order_id", nullable = false, columnDefinition = "varchar(12) comment '고유 코드'")
    private String orderId;

    @Column(name = "quantity", nullable = false, columnDefinition = "INT UNSIGNED comment '구매 수량'")
    private int quantity;



    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "varchar(20) comment '상태'")
    private LocalDateTime createdAt;

    public static ProductOrder create(ProductSnapshot productSnapshot, DeliveryAddressSnapshot deliveryAddressSnapshot){
        return ProductOrder.builder()
            .productSnapshot(productSnapshot)
            .deliveryAddressSnapshot(deliveryAddressSnapshot)
            .build();
    }

    public static ProductOrder create(ProductSnapshot productSnapshot, DeliveryAddressSnapshot deliveryAddressSnapshot, String orderId, int quantity) {
        return ProductOrder.builder()
            .productSnapshot(productSnapshot)
            .deliveryAddressSnapshot(deliveryAddressSnapshot)
            .orderId(orderId)
            .quantity(quantity)
            .productOrderStatus(ProductOrderStatus.REQUESTED)
            .build();
    }

    public void paymentSuccess(){
        this.productOrderStatus = ProductOrderStatus.APPROVED;
    }

    public void paymentFail(){
        this.productOrderStatus = ProductOrderStatus.FAILED;
    }
}
