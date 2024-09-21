package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
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

    public List<DeliveryAddress> getActiveDeliveryAddressesByMemberId(Long memberId) {
        return deliveryAddressJpaRepository.findAllByMemberIdAndDeletedAtIsNull(memberId);
    }

    public DeliveryAddress getActiveDeliveryAddressByIdAndMemberId(Long id, Long memberId) {
        return deliveryAddressJpaRepository.findByIdAndMemberIdAndDeletedAtIsNull(id, memberId)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_DELIVERY_ADDRESS));
    }

}
