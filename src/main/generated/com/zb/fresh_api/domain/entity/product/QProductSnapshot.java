package com.zb.fresh_api.domain.entity.product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductSnapshot is a Querydsl query type for ProductSnapshot
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductSnapshot extends EntityPathBase<ProductSnapshot> {

    private static final long serialVersionUID = -86558155L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductSnapshot productSnapshot = new QProductSnapshot("productSnapshot");

    public final com.zb.fresh_api.domain.entity.category.QCategory category;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.zb.fresh_api.domain.entity.member.QMember member;

    public final StringPath name = createString("name");

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final QProduct product;

    public final DateTimePath<java.time.LocalDateTime> snapshotDate = createDateTime("snapshotDate", java.time.LocalDateTime.class);

    public QProductSnapshot(String variable) {
        this(ProductSnapshot.class, forVariable(variable), INITS);
    }

    public QProductSnapshot(Path<? extends ProductSnapshot> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductSnapshot(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductSnapshot(PathMetadata metadata, PathInits inits) {
        this(ProductSnapshot.class, metadata, inits);
    }

    public QProductSnapshot(Class<? extends ProductSnapshot> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.zb.fresh_api.domain.entity.category.QCategory(forProperty("category"), inits.get("category")) : null;
        this.member = inits.isInitialized("member") ? new com.zb.fresh_api.domain.entity.member.QMember(forProperty("member")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

