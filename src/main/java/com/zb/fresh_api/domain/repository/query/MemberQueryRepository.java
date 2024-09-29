package com.zb.fresh_api.domain.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.domain.dto.member.MemberWithPoint;
import com.zb.fresh_api.domain.entity.member.QMember;
import com.zb.fresh_api.domain.entity.point.QPoint;
import com.zb.fresh_api.domain.enums.member.MemberStatus;
import com.zb.fresh_api.domain.enums.member.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    QMember member = QMember.member;
    QPoint point = QPoint.point;

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

    public MemberWithPoint findMemberWithPointByEmailAndProvider(String email, Provider provider) {
        return jpaQueryFactory
                .select(
                        Projections
                                .constructor(
                                        MemberWithPoint.class,
                                        member,
                                        point
                                )
                )
                .from(member)
                .innerJoin(point).on(point.member.eq(member))
                .where(
                        isNotWithdrawal(member),
                        isActive(member),
                        eqEmail(member, email),
                        eqProvider(member, provider)
                )
                .fetchOne();
    }

    private BooleanExpression eqEmail(QMember member, String email) {
        return member.email.eq(email);
    }

    private BooleanExpression eqProvider(QMember member, Provider provider) {
        return member.provider.eq(provider);
    }

    private BooleanExpression isActive(QMember member) {
        return member.status.eq(MemberStatus.ACTIVE);
    }

    private BooleanExpression eqNickname(QMember member, String nickname) {
        return member.nickname.eq(nickname);
    }

    private BooleanExpression isNotWithdrawal(QMember member) {
        return member.deletedAt.isNull();
    }

}
