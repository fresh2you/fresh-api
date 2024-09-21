package com.zb.fresh_api.domain.entity.point;

import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.enums.point.PointStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
    name = "point"
)
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @Column(name = "balance", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) comment '잔액'")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_status", nullable = false, columnDefinition = "varchar(20) comment '상태'")
    private PointStatus pointStatus;

    public static Point create(Member member, BigDecimal balance, PointStatus pointStatus) {
        return Point.builder()
            .member(member)
            .balance(balance)
            .pointStatus(pointStatus)
            .build();
    }

    public void charge(BigDecimal point){
        this.balance = balance.add(point);
    }
}
