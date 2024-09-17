package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.product.ProductLike;
import com.zb.fresh_api.domain.repository.jpa.ProductLikeJpaRepository;
import com.zb.fresh_api.domain.repository.query.ProductLikeQueryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class ProductLikeReader {
    private final ProductLikeJpaRepository productLikeJpaRepository;
    private final ProductLikeQueryRepository productLikeQueryRepository;
    public boolean existsByProductIdAndMemberId(Long productId,  Long memberId){
        return productLikeJpaRepository.existsByProductIdAndMemberId(productId, memberId);
    }

    public List<ProductLike> findByProductId(Long productId){
        return productLikeJpaRepository.findByProductId(productId);
    }

    public List<Long> findProductIdByMemberId(Long memberId){
        return productLikeQueryRepository.findByMemberId(memberId);
    }

    public ProductLike store(ProductLike productLike){
        return productLikeJpaRepository.save(productLike);
    }
}
