package com.zb.fresh_api.domain.entity.address;

import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "delivery_address"
)
public class DeliveryAddress extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '배송지 고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

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

    @Column(name = "is_default", columnDefinition = "TINYINT(1) comment '기본 배송지 여부'")
    private Boolean isDefault;

}
