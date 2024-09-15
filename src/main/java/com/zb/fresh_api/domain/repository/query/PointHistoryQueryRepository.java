package com.zb.fresh_api.domain.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.entity.point.QPointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointHistoryQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QPointHistory pointHistory = QPointHistory.pointHistory;
}
