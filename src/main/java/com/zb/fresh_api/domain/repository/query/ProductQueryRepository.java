package com.zb.fresh_api.domain.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zb.fresh_api.api.dto.request.GetAllProductByConditionsRequest;
import com.zb.fresh_api.api.dto.request.GetSellerProducts;
import com.zb.fresh_api.domain.dto.recommend.CategoryProductOrder;
import com.zb.fresh_api.domain.dto.recommend.RecommendProductSummary;
import com.zb.fresh_api.domain.dto.recommend.SellerProductOrder;
import com.zb.fresh_api.domain.entity.address.QDeliveryAddress;
import com.zb.fresh_api.domain.entity.address.QDeliveryAddressSnapshot;
import com.zb.fresh_api.domain.entity.category.QCategory;
import com.zb.fresh_api.domain.entity.member.QMember;
import com.zb.fresh_api.domain.entity.order.QProductOrder;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.entity.product.QProduct;
import com.zb.fresh_api.domain.entity.product.QProductSnapshot;
import com.zb.fresh_api.domain.repository.jpa.ProductOrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ProductOrderJpaRepository productOrderJpaRepository;

    QMember member = QMember.member;
    QProduct product = QProduct.product;
    QCategory category = QCategory.category;
    QProductOrder productOrder = QProductOrder.productOrder;
    QProductSnapshot productSnapshot = QProductSnapshot.productSnapshot;
    QDeliveryAddress deliveryAddress = QDeliveryAddress.deliveryAddress;
    QDeliveryAddressSnapshot deliveryAddressSnapshot = QDeliveryAddressSnapshot.deliveryAddressSnapshot;

    public Page<Product> findAll(GetAllProductByConditionsRequest request) {
        BooleanBuilder builder = new BooleanBuilder();

        if (request.categoryId() != null) {
            builder.and(product.category.id.eq(request.categoryId()));
        }

        if (request.keyword() != null && !request.keyword().isEmpty()) {
            builder.and(product.name.containsIgnoreCase(request.keyword())
                .or(product.description.containsIgnoreCase(request.keyword()))
                .or(product.member.nickname.containsIgnoreCase(request.keyword())));
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

    public Page<Product> findByMemberId(GetSellerProducts request, Long memberId){
        Pageable pageable = PageRequest.of(request.page(), request.size());
        JPAQuery<Product> query = jpaQueryFactory.selectFrom(product)
            .where(product.member.id.eq(memberId))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        List<Product> products = query.fetch();
        long total = query.fetchCount();

        return new PageImpl<>(products, pageable, total);

    }

    public List<RecommendProductSummary> findAllRandomProduct(int size) {
        return jpaQueryFactory
                .select(
                        Projections
                                .constructor(
                                        RecommendProductSummary.class,
                                        product.id,
                                        product.name,
                                        product.productImage,
//                                        product.member.nickname.as("sellerName"),
                                        product.member.nickname,
                                        product.price,
//                                        product.description,
                                        new CaseBuilder()
                                                .when(product.description.length().gt(17))
                                                .then(product.description.substring(0, 17).concat("...")) // 20글자까지 잘라서 "..." 추가
                                                .otherwise(product.description)
//                                        product.description.length().gt(20)
                                ))
                .from(product)
                .innerJoin(member).on(product.member.eq(member))
                .where(isNotDeleted(product))
                .orderBy(Expressions
                        .numberTemplate(
                                Double.class,
                                "function('rand')").asc()
                )
                .limit(size)
                .fetch();
    }

    public List<RecommendProductSummary> findAllRecommendProduct(Long memberId, int size) {
        // 1. 카테고리별 구매 이력 조회
        List<CategoryProductOrder> categoryProductOrders = jpaQueryFactory
                .select(
                        Projections
                                .constructor(
                                        CategoryProductOrder.class,
                                        category.id,
                                        productOrder.count().as("count")
                                )
                )
                .from(productOrder)
                .innerJoin(productSnapshot).on(productOrder.productSnapshot.eq(productSnapshot))
                .innerJoin(product).on(productSnapshot.product.eq(product))
                .innerJoin(category).on(product.category.eq(category))
                .innerJoin(deliveryAddressSnapshot).on(productOrder.deliveryAddressSnapshot.eq(deliveryAddressSnapshot))
                .innerJoin(deliveryAddress).on(deliveryAddressSnapshot.deliveryAddress.eq(deliveryAddress))
                .where(deliveryAddress.member.id.eq(memberId))
                .groupBy(QCategory.category.id)
                .fetch();

        // 2. 판매자별 구매 이력 조회
        List<SellerProductOrder> sellerProductOrders = jpaQueryFactory
                .select(
                        Projections
                                .constructor(
                                        SellerProductOrder.class,
                                        member.id,
                                        productOrder.count().as("count")
                                )
                )
                .from(productOrder)
                .innerJoin(productSnapshot).on(productOrder.productSnapshot.eq(productSnapshot))
                .innerJoin(product).on(productSnapshot.product.eq(product))
                .innerJoin(member).on(product.member.eq(member))
                .innerJoin(deliveryAddressSnapshot).on(productOrder.deliveryAddressSnapshot.eq(deliveryAddressSnapshot))
                .where(deliveryAddress.member.id.eq(memberId))
                .groupBy(product.member.id)
                .fetch();

        // 3. 가중치 계산
        Map<Long, Long> categoryMap = categoryProductOrders.stream()
                .collect(Collectors.toMap(
                        CategoryProductOrder::categoryId,
                        CategoryProductOrder::count
                ));

        categoryMap.forEach((key, value) -> System.out.println("Category ID: " + key + ", Count: " + value));

        Map<Long, Long> sellerMap = sellerProductOrders.stream()
                .collect(Collectors.toMap(
                        SellerProductOrder::sellerId,
                        SellerProductOrder::count
                ));

//        sellerMap.forEach((key, value) -> System.out.println("seller ID: " + key + ", Count: " + value));

        // Step 4: 추천 상품 조회 (카테고리, 판매자 가중치를 기반으로)
        List<RecommendProductSummary> recommendedProducts = jpaQueryFactory
                .select(
                        Projections
                                .constructor(
                                        RecommendProductSummary.class,
                                        product.id,
                                        product.name,
                                        product.productImage,
                                        product.member.nickname,
                                        product.price,
//                                        product.description,
                                        new CaseBuilder()
                                                .when(product.description.length().gt(17))
                                                .then(product.description.substring(0, 17).concat("...")) // 20글자까지 잘라서 "..." 추가
                                                .otherwise(product.description)
                                )
                )
                .from(product)
                .leftJoin(category).on(product.category.eq(category))
                .leftJoin(member).on(product.member.eq(member))
                .where(
                        isNotDeleted(product)
                )
                // TODO Map get() 오류 해결해야함.
                .orderBy(
                        combineOrderSpecifiers(
                                createCategoryOrderSpecifiers(categoryMap), createSellerOrderSpecifiers(sellerMap))
                )
                .limit(size * 2L)
                .fetch();

        Collections.shuffle(recommendedProducts);

        return recommendedProducts.stream()
                .limit(size)
                .toList();
    }

    private BooleanExpression isNotDeleted(QProduct product) {
        return product.deletedAt.isNull();
    }

    private OrderSpecifier<?>[] combineOrderSpecifiers(OrderSpecifier<?>[]... orderSpecifierArrays) {
        List<OrderSpecifier<?>> combinedList = new ArrayList<>();

        for (OrderSpecifier<?>[] array : orderSpecifierArrays) {
            combinedList.addAll(Arrays.asList(array));
        }

        return combinedList.toArray(new OrderSpecifier[0]);
    }

    private OrderSpecifier<?>[] createCategoryOrderSpecifiers(Map<Long, Long> categoryMap) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : categoryMap.entrySet()) {
            Long categoryId = entry.getKey();
            Long count = entry.getValue();

            orderSpecifiers.add(
                    new CaseBuilder()
                            .when(category.id.eq(categoryId))
                            .then(count)
                            .otherwise(0L)
                            .desc()
            );
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]); // OrderSpecifier 배열로 변환하여 반환
    }

    private OrderSpecifier<?>[] createSellerOrderSpecifiers(Map<Long, Long> sellerMap) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : sellerMap.entrySet()) {
            Long sellerId = entry.getKey();
            Long count = entry.getValue();

            orderSpecifiers.add(
                    new CaseBuilder()
                            .when(member.id.eq(sellerId))
                            .then(count)
                            .otherwise(0L)
                            .desc()
            );
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]); // OrderSpecifier 배열로 변환하여 반환
    }
}
