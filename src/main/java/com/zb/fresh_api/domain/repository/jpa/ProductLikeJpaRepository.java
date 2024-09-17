package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.product.ProductLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLikeJpaRepository extends JpaRepository<ProductLike, Long> {
    boolean existsByProductIdAndMemberId(Long productId,  Long memberId);
    List<ProductLike> findByProductId(Long productId);
    List<ProductLike> findByMemberId(Long memberId);
    Optional<ProductLike> findByProductIdAndMemberId(Long productId, Long memberId);
}
