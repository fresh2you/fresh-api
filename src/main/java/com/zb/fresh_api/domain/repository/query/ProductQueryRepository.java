package com.zb.fresh_api.domain.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.api.dto.request.GetAllProductByConditionsRequest;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.entity.product.QProduct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QProduct product = QProduct.product;

    public Page<Product> findAll(GetAllProductByConditionsRequest request) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.categoryId() != null) {
            builder.and(product.category.id.eq(request.categoryId()));
        }

        if (request.keyword() != null && !request.keyword().isEmpty()) {
            builder.and(product.name.containsIgnoreCase(request.keyword()));
        }
        Pageable pageable = PageRequest.of(request.page(), request.size());
        JPAQuery<Product> query = jpaQueryFactory.selectFrom(product)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());


        List<Product> products = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(products, pageable, total);
    }
}
