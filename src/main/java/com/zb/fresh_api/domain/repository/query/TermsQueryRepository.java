package com.zb.fresh_api.domain.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.entity.terms.QTerms;
import com.zb.fresh_api.domain.entity.terms.Terms;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TermsQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    QTerms terms = QTerms.terms;

    public List<Terms> findAll(){
        return jpaQueryFactory
            .selectFrom(terms)
            .fetch();
    }
}
