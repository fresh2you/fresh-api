package com.zb.fresh_api.domain.entity.point;

import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.enums.point.PointTransactionType;
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
    name = "point_history"
)
public class PointHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 포인트 번호'")
    private Point point;

    //    Todo 오더 추가 후 작업
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '오더 고유 번호'")
//    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_transaction_type", nullable = false, columnDefinition = "varchar(20) comment '거래유형'")
    private PointTransactionType pointTransactionType;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) comment '거래 금액'")
    private BigDecimal amount;




    @Column(name = "balanceBefore", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) comment '거래 전 잔액'")
    private BigDecimal balanceBefore;

    @Column(name = "balanceAfter", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) comment '거래 후 잔액'")
    private BigDecimal balanceAfter;

    @Column(name = "description", nullable = true, columnDefinition = "varchar(255) comment '거래 설명'")
    private String description;

    public static PointHistory create(Point point, PointTransactionType transactionType, BigDecimal amount,
        BigDecimal balanceBefore, BigDecimal balanceAfter, String description){
        return PointHistory.builder()
            .point(point)
            .pointTransactionType(transactionType)
            .amount(amount)
            .balanceBefore(balanceBefore)
            .balanceAfter(balanceAfter)
            .description(description)
            .build();
    }
}
