package com.zb.fresh_api.domain.entity.address;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "delivery_address_snapshot"
)
public class DeliveryAddressSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '배송지 스냅샷 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", columnDefinition = "BIGINT UNSIGNED comment '배송지 고유 번호'")
    private DeliveryAddress deliveryAddress;

    @Column(name = "recipient_name", nullable = false, columnDefinition = "varchar(50) comment '받는 사람 이름'")
    private String recipientName;

    @Column(name = "phone", nullable = false, columnDefinition = "varchar(20) comment '전화번호'")
    private String phone;

    @Column(name = "address", nullable = false, columnDefinition = "varchar(255) comment '주소'")
    private String address;

    @Column(name = "detailed_address", columnDefinition = "varchar(255) comment '상세 주소'")
    private String detailedAddress;

    @Column(name = "postal_code", nullable = false, columnDefinition = "varchar(10) comment '우편번호'")
    private String postalCode;

    @Builder.Default
    @Column(name = "created_at", updatable = false, columnDefinition = "datetime comment '스냅샷 생성 일시'")
    private LocalDateTime createdAt = LocalDateTime.now();

    public static DeliveryAddressSnapshot of(DeliveryAddress deliveryAddress) {
        return DeliveryAddressSnapshot.builder()
                .deliveryAddress(deliveryAddress)
                .recipientName(deliveryAddress.getRecipientName())
                .phone(deliveryAddress.getPhone())
                .address(deliveryAddress.getAddress())
                .detailedAddress(deliveryAddress.getDetailedAddress())
                .postalCode(deliveryAddress.getPostalCode())
                .build();
    }

}
