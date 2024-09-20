package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.address.DeliveryAddress;
import com.zb.fresh_api.domain.repository.jpa.DeliveryAddressJpaRepository;
import com.zb.fresh_api.domain.repository.query.DeliveryAddressQueryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Reader
@RequiredArgsConstructor
public class DeliveryAddressReader {

    private final DeliveryAddressJpaRepository deliveryAddressJpaRepository;
    private final DeliveryAddressQueryRepository deliveryAddressQueryRepository;

    public List<DeliveryAddress> findActiveDeliveryAddressesByMemberId(Long memberId) {
        return deliveryAddressJpaRepository.findAllByMemberIdAndDeletedAtIsNull(memberId);
    }

}
