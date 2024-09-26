package com.zb.fresh_api.domain.repository.query;

import static com.zb.fresh_api.domain.entity.address.QDeliveryAddress.deliveryAddress;
import static com.zb.fresh_api.domain.entity.address.QDeliveryAddressSnapshot.deliveryAddressSnapshot;
import static com.zb.fresh_api.domain.entity.member.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.entity.order.QProductOrder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QProductOrder productOrder = QProductOrder.productOrder;

    public boolean existsByProductIdAndMemberId(Long productId, Long memberId) {
        Integer fetchOne = jpaQueryFactory
            .selectOne()
            .from(productOrder)
            .where(productOrder.productSnapshot.id.eq(productId)
                .and(productOrder.deliveryAddressSnapshot.deliveryAddress.member.id.eq(memberId)))
            .fetchFirst();
        return fetchOne != null;
    }

    public List<Long> findProductIdsByMemberId(Long memberId) {
        return jpaQueryFactory
            .select(productOrder.productSnapshot.product.id)
            .from(productOrder)
            .join(productOrder.deliveryAddressSnapshot, deliveryAddressSnapshot)
            .join(deliveryAddressSnapshot.deliveryAddress, deliveryAddress)
            .join(deliveryAddress.member, member)
            .where(deliveryAddress.member.id.eq(memberId))
            .fetch();
    }

}
