package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.order.ProductOrder;
import com.zb.fresh_api.domain.repository.jpa.ProductOrderJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class ProductOrderWriter {
    private final ProductOrderJpaRepository productOrderJpaRepository;

    public ProductOrder store(ProductOrder productOrder){
        return productOrderJpaRepository.save(productOrder);
    }
}
