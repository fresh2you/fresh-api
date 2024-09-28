package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.address.DeliveryAddressSnapshot;
import com.zb.fresh_api.domain.repository.jpa.DeliveryAddressSnapshotJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class DeliveryAddressSnapshotWriter {
    private final DeliveryAddressSnapshotJpaRepository deliveryAddressSnapshotJpaRepository;

    public DeliveryAddressSnapshot store(DeliveryAddressSnapshot deliveryAddressSnapshot){
        return deliveryAddressSnapshotJpaRepository.save(deliveryAddressSnapshot);
    }
}
