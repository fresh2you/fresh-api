package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.order.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderJpaRepository extends JpaRepository<ProductOrder, Long> {

}
