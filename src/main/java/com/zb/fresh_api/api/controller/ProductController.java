package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.annotation.LoginMember;
import com.zb.fresh_api.api.dto.request.AddProductRequest;
import com.zb.fresh_api.api.dto.request.DeleteProductRequest;
import com.zb.fresh_api.api.dto.request.GetAllProductByConditionsRequest;
import com.zb.fresh_api.api.dto.request.UpdateProductRequest;
import com.zb.fresh_api.api.dto.response.AddProductResponse;
import com.zb.fresh_api.api.dto.response.DeleteProductResponse;
import com.zb.fresh_api.api.dto.response.FindAllProductLikeResponse;
import com.zb.fresh_api.api.dto.response.GetAllProductByConditionsResponse;
import com.zb.fresh_api.api.dto.response.GetProductDetailResponse;
import com.zb.fresh_api.api.dto.response.LikeProductResponse;
import com.zb.fresh_api.api.dto.response.UpdateProductResponse;
import com.zb.fresh_api.api.service.ProductService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import com.zb.fresh_api.domain.entity.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AddProductResponse>> addProduct(
        @Parameter(hidden = true)@LoginMember Member member,
        @Parameter @RequestPart(value = "request", required = true) AddProductRequest request,
        @Parameter @RequestPart(value = "image", required = false) MultipartFile image) {
        AddProductResponse storedProductId = productService.addProduct(request, member, image);
        return ApiResponse.success(ResponseCode.SUCCESS, storedProductId );
    }


    @Operation(
        summary = "상품 상세 조회",
        description = "상품 ID를 통해 상품을 상세 조회합니다."
    )
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<GetProductDetailResponse>> getProduct(@PathVariable(value = "productId") Long productId) {
        GetProductDetailResponse productDetail = productService.getProductDetail(productId);
        return ApiResponse.success(ResponseCode.SUCCESS, productDetail);
    }

    @Operation(
        summary = "상품 수정",
        description = "상품 수정을 위한 API입니다."
    )
    @PatchMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UpdateProductResponse>> updateProduct(
        @PathVariable(name = "productId") Long productId,
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @Parameter @RequestPart(value = "request", required = false) UpdateProductRequest request,
        @Parameter @RequestPart(value = "image",required = false) MultipartFile image) {
        UpdateProductResponse updateProductResponse = productService.updateProduct(productId,
            request, image, loginMember);
        return ApiResponse.success(ResponseCode.SUCCESS,updateProductResponse);
    }


    @Operation(
        summary = "상품 삭제",
        description = "상품 삭제를 위한 API입니다."
    )
    @DeleteMapping
    public ResponseEntity<ApiResponse<DeleteProductResponse>> deleteProduct(DeleteProductRequest request,
        @Parameter(hidden = true) @LoginMember Member member) {
        DeleteProductResponse deleteProductId = productService.deleteProduct(request, member);
        return ApiResponse.success(ResponseCode.SUCCESS,deleteProductId);

    }

    // TODO 키워드
    //  제목
    //  상품 설명
    //  판매자 이름
    @Operation(
        summary = "상품 목록 조회",
        description = "키워드 또는 카테고리 타입 등을 통해 상품을 상세 조회합니다."
    )
    @GetMapping()
    public ResponseEntity<ApiResponse<GetAllProductByConditionsResponse>> getAllProductByConditions(
        GetAllProductByConditionsRequest request) {
        GetAllProductByConditionsResponse products = productService.findProducts(request);
        return ApiResponse.success(ResponseCode.SUCCESS, products);
    }

    @Operation(
        summary = "좋아요 상품 목록 조회",
        description = "키워드 또는 카테고리 타입 등을 통해 상품을 상세 조회합니다."
    )
    @GetMapping("/like")
    public ResponseEntity<ApiResponse<FindAllProductLikeResponse>> getAllProductByLike(
        @Parameter(hidden = true) @LoginMember Member loginMember) {
        FindAllProductLikeResponse productList = productService.findAllProductLike(
            loginMember.getId());
        return ApiResponse.success(ResponseCode.SUCCESS, productList);
    }

    @Operation(
        summary = "상품 좋아요",
        description = "상품 좋아요 추가 "
    )
    @PostMapping("/{productId}/like")
    public ResponseEntity<ApiResponse<LikeProductResponse>> setProductLike(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable(name = "productId") Long productId) {
        LikeProductResponse likeProductResponse = productService.like(productId, loginMember.getId());
        return ApiResponse.success(ResponseCode.SUCCESS,likeProductResponse);
    }

    @Operation(
        summary = "상품 좋아요 취소",
        description = "상품의 좋아요 삭제"
    )
    @DeleteMapping("/{productId}/like")
    public ResponseEntity<ApiResponse<Void>> setProductUnLike(
        @Parameter(hidden = true) @LoginMember Member loginMember,
        @PathVariable(name = "productId") Long productId) {
        productService.unLike(productId, loginMember.getId());
        return ApiResponse.success(ResponseCode.SUCCESS);
    }
}
