package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.product.ProductLike;
import com.zb.fresh_api.domain.repository.jpa.ProductLikeJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class ProductLikeWriter {
    private final ProductLikeJpaRepository productLikeJpaRepository;

    public ProductLike store(ProductLike productLike){
        return productLikeJpaRepository.save(productLike);
    }

    public void delete(ProductLike productLike){
        productLikeJpaRepository.delete(productLike);
    }
}
