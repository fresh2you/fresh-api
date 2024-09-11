package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.annotation.LoginMember;
import com.zb.fresh_api.api.dto.request.AddProductRequest;
import com.zb.fresh_api.api.dto.response.AddProductResponse;
import com.zb.fresh_api.api.service.ProductService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import com.zb.fresh_api.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "상품 API",
    description = "상품 관련 API"
)
@RestController
@RequestMapping("/v1/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;


    @Operation(
        summary = "상품 등록",
        description = "상품을 등록합니다."
    )
    @PostMapping
    public ResponseEntity<ApiResponse<AddProductResponse>> addProduct(@Valid @RequestBody
        AddProductRequest request, @LoginMember Member member) {
        AddProductResponse storedProductId = productService.addProduct(request, member);
        return ApiResponse.success(ResponseCode.SUCCESS, storedProductId );
    }
}
