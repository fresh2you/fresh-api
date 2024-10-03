package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.request.LoadProductRecommendListRequest;
import com.zb.fresh_api.api.dto.response.LoadProductRecommendListResponse;
import com.zb.fresh_api.domain.dto.recommend.RecommendProductSummary;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.repository.reader.OrderReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {

    private final OrderReader orderReader;
    private final ProductReader productReader;

    @Transactional(readOnly = true)
    public LoadProductRecommendListResponse loadProductRecommendList(final Member member,
                                                                     final LoadProductRecommendListRequest request) {
        // 1. 상품 구매 여부
        final List<RecommendProductSummary> products;
        final boolean hasProductOrder = orderReader.existsProductOrderByMemberId(member);

        if (!hasProductOrder) {
            // 1. 구매 이력이 없을 경우, 랜덤 출력 조건 추가
            products = productReader.getAllRandomProduct(request.size());
        } else {
            // 2. 구매 이력이 있을 경우, 카테고리 및 판매자 상품 비율로 출력
            products = productReader.getAllRecommendProduct(member, request.size());
        }

        return new LoadProductRecommendListResponse(products, products.size());
    }

    @Transactional(readOnly = true)
    public LoadProductRecommendListResponse loadRandomProductRecommendList(final LoadProductRecommendListRequest request) {
        final List<RecommendProductSummary> products = productReader.getAllRandomProduct(request.size());
        return new LoadProductRecommendListResponse(products, products.size());
    }

}
