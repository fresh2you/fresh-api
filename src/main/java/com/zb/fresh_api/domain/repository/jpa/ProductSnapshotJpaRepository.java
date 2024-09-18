package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.product.ProductSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSnapshotJpaRepository extends JpaRepository<ProductSnapshot, Long> {

}
