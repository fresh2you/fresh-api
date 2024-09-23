package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.address.DeliveryAddress;
import com.zb.fresh_api.domain.repository.jpa.DeliveryAddressJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class DeliveryAddressWriter {

    private final DeliveryAddressJpaRepository deliveryAddressJpaRepository;

    public DeliveryAddress store(final DeliveryAddress deliveryAddress) {
        return deliveryAddressJpaRepository.save(deliveryAddress);
    }
}
