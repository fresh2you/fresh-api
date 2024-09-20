package com.zb.fresh_api.domain.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.entity.address.QDeliveryAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryAddressQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QDeliveryAddress deliveryAddress = QDeliveryAddress.deliveryAddress;


}
