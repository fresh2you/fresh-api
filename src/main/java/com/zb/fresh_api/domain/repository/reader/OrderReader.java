package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.repository.jpa.OrderJpaRepository;
import com.zb.fresh_api.domain.repository.query.OrderQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class OrderReader {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderQueryRepository orderQueryRepository;

    public boolean existsByProductIdAndMemberId(Long productId, Long memberId){
        return orderQueryRepository.existsByProductIdAndMemberId(productId,memberId);
    }

    public List<Long> findProductIdsByMemberId(Long memberId){
        return orderQueryRepository.findProductIdsByMemberId(memberId);
    }
}