package com.zb.fresh_api.domain.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.entity.category.QCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QCategory category = QCategory.category;

    public List<Category> findAll() {
        return jpaQueryFactory
            .select(category)
            .from(category)
            .leftJoin(category.parent).fetchJoin()
            .fetch();
    }
}
