package com.zb.fresh_api.domain.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.entity.order.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QOrder order = QOrder.order;

    public boolean existsByProductIdAndMemberId(Long productId, Long memberId) {
        Integer fetchOne = jpaQueryFactory
            .selectOne()
            .from(order)
            .where(order.productSnapshot.id.eq(productId)
                .and(order.customer.member.id.eq(memberId)))
            .fetchFirst();
        return fetchOne != null;
    }
}
