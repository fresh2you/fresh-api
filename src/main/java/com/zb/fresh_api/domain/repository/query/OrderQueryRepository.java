package com.zb.fresh_api.domain.repository.query;

import static com.zb.fresh_api.domain.entity.address.QDeliveryAddress.deliveryAddress;
import static com.zb.fresh_api.domain.entity.address.QDeliveryAddressSnapshot.deliveryAddressSnapshot;
import static com.zb.fresh_api.domain.entity.member.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.entity.address.QDeliveryAddress;
import com.zb.fresh_api.domain.entity.address.QDeliveryAddressSnapshot;
import com.zb.fresh_api.domain.entity.member.QMember;
import com.zb.fresh_api.domain.entity.order.QProductOrder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QMember member = QMember.member;
    QProductOrder productOrder = QProductOrder.productOrder;
    QDeliveryAddress deliveryAddress = QDeliveryAddress.deliveryAddress;
    QDeliveryAddressSnapshot deliveryAddressSnapshot = QDeliveryAddressSnapshot.deliveryAddressSnapshot;

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

    public boolean existsProductOrderByMemberId(Long memberId) {
        return jpaQueryFactory
                .selectOne()
                .from(deliveryAddressSnapshot)
                .innerJoin(deliveryAddress).on(deliveryAddressSnapshot.deliveryAddress.eq(deliveryAddress))
                .innerJoin(member).on(deliveryAddress.member.eq(member))
                .where(
                        member.id.eq(memberId)
                )
                .fetchFirst() != null;
    }

}
