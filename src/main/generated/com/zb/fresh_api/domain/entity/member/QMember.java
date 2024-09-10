package com.zb.fresh_api.domain.entity.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -778896561L;

    public static final QMember member = new QMember("member1");

    public final com.zb.fresh_api.domain.entity.base.QBaseTimeEntity _super = new com.zb.fresh_api.domain.entity.base.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<com.zb.fresh_api.domain.enums.member.Provider> provider = createEnum("provider", com.zb.fresh_api.domain.enums.member.Provider.class);

    public final StringPath providerId = createString("providerId");

    public final EnumPath<com.zb.fresh_api.domain.enums.member.MemberRole> role = createEnum("role", com.zb.fresh_api.domain.enums.member.MemberRole.class);

    public final EnumPath<com.zb.fresh_api.domain.enums.member.MemberStatus> status = createEnum("status", com.zb.fresh_api.domain.enums.member.MemberStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

