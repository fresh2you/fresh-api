package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.repository.jpa.ProductJpaRepository;
import com.zb.fresh_api.domain.repository.query.ProductQueryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class ProductReader {
    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryRepository productQueryRepository;

    public Optional<Product> findById(Long id){
        return productJpaRepository.findById(id);
    }
}
