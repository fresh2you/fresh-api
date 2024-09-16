package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.repository.jpa.ProductJpaRepository;
import com.zb.fresh_api.domain.repository.query.ProductQueryRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class ProductWriter {

    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryRepository productQueryRepository;

    public Product store(Product product) {
        return productJpaRepository.save(product);
    }

}
