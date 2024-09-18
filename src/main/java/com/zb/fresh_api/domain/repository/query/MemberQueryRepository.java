package com.zb.fresh_api.domain.repository.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.entity.member.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QMember member = QMember.member;

    public boolean existActiveEmail(String email) {
        return jpaQueryFactory
            .selectOne()
            .from(member)
            .where(
                eqEmail(member, email),
                isNotWithdrawal(member)
            )
            .fetchFirst() != null;
    }

    public boolean existActiveNickname(String nickname) {
        return jpaQueryFactory
            .selectOne()
            .from(member)
            .where(
                eqNickname(member, nickname),
                isNotWithdrawal(member)
            )
            .fetchFirst() != null;
    }

    public boolean existsByPhone(String phone) {
        QMember member = QMember.member;

        Long count = jpaQueryFactory
            .select(member.count())
            .from(member)
            .where(member.phone.eq(phone))
            .fetchOne();

        return count != null && count > 0;
    }
    private BooleanExpression eqEmail(QMember member, String email) {
        return member.email.eq(email);
    }

    private BooleanExpression eqNickname(QMember member, String nickname) {
        return member.nickname.eq(nickname);
    }

    private BooleanExpression isNotWithdrawal(QMember member) {
        return member.deletedAt.isNull();
    }

}
