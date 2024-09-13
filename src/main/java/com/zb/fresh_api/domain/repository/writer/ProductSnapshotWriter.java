package com.zb.fresh_api.domain.repository.writer;

import com.zb.fresh_api.domain.annotation.Writer;
import com.zb.fresh_api.domain.entity.product.ProductSnapshot;
import com.zb.fresh_api.domain.repository.jpa.ProductSnapshotJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class ProductSnapshotWriter {
    private final ProductSnapshotJpaRepository productSnapshotJpaRepository;

    public ProductSnapshot store(ProductSnapshot productSnapshot) {
        return productSnapshotJpaRepository.save(productSnapshot);
    }
}
