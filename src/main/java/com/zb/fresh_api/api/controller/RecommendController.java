package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.annotation.LoginMember;
import com.zb.fresh_api.api.dto.request.LoadProductRecommendListRequest;
import com.zb.fresh_api.api.dto.response.LoadProductRecommendListResponse;
import com.zb.fresh_api.api.service.RecommendService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import com.zb.fresh_api.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "사용자 API",
        description = "사용자와 관련된 API"
)
@RestController
@RequestMapping("/v1/api/recommendations")
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @Operation(
            summary = "사용자 기반 추천 서비스",
            description = "사용자의 주문 내역을 기반으로 상품을 추천합니다."
    )
    @GetMapping("/order-history")
    public ResponseEntity<ApiResponse<LoadProductRecommendListResponse>> loadProductRecommendList(
            @Parameter(hidden = true) @LoginMember Member loginMember,
            @Valid LoadProductRecommendListRequest request
    ) {
        return ApiResponse.success(ResponseCode.SUCCESS, recommendService.loadRecommendedProductList(loginMember, request));
    }
}
