package com.zb.fresh_api.domain.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.entity.product.QProductLike;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductLikeQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QProductLike productLike = QProductLike.productLike;

    public List<Long> findByMemberId(Long memberId){

        return jpaQueryFactory.select(productLike.product.id)
            .from(productLike)
            .where(productLike.member.id.eq(memberId))
            .fetch();
    }
}
