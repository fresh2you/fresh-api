package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.address.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressJpaRepository extends JpaRepository<DeliveryAddress, Long> {

    List<DeliveryAddress> findAllByMemberIdAndDeletedAtIsNull(Long memberId);

    Optional<DeliveryAddress> findByIdAndMemberIdAndDeletedAtIsNull(final Long id, final Long memberId);

}
