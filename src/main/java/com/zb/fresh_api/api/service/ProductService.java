package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.request.AddProductRequest;
import com.zb.fresh_api.api.dto.request.GetProductDetailRequest;
import com.zb.fresh_api.api.dto.response.AddProductResponse;
import com.zb.fresh_api.api.dto.response.GetProductDetailResponse;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.repository.reader.CategoryReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import com.zb.fresh_api.domain.repository.writer.ProductWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductWriter productWriter;
    private final ProductReader productReader;
    private final CategoryReader categoryReader;
    @Transactional
    public AddProductResponse addProduct(AddProductRequest request, Member member,
        MultipartFile image) {
        if (!member.isSeller()) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_ACCESS_EXCEPTION);
        }
        Category category = categoryReader.findById(request.categoryId()).orElseThrow(
            () -> new CustomException(ResponseCode.CATEGORY_NOT_VALID)
        );

        // TODO 1. 이미지 변환 (S3)
        final String profileImage = null;

        Product storedProduct = productWriter.store(Product.create(request, category, member, profileImage));
        return new AddProductResponse(storedProduct.getId(), storedProduct.getName(), storedProduct.getCreatedAt());
    }

    public GetProductDetailResponse getProductDetail(GetProductDetailRequest request) {
        Product product = productReader.findById(request.productId())
            .orElseThrow(() -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND));

        return GetProductDetailResponse.fromEntity(product);
    }
}
