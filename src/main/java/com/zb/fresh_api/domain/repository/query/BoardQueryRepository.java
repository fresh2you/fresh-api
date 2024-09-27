package com.zb.fresh_api.domain.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.entity.board.QBoard;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QBoard board = QBoard.board;

    public List<Long> findProductIdsByMemberId(final Long memberId) {
        return jpaQueryFactory
            .select(board.product.id)
            .from(board)
            .where(board.member.id.eq(memberId))
            .fetch();
    }
}
