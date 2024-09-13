package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.request.AddProductRequest;
import com.zb.fresh_api.api.dto.request.GetProductDetailRequest;
import com.zb.fresh_api.api.dto.request.UpdateProductRequest;
import com.zb.fresh_api.api.dto.response.AddProductResponse;
import com.zb.fresh_api.api.dto.response.GetProductDetailResponse;
import com.zb.fresh_api.api.dto.response.UpdateProductResponse;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.entity.product.ProductSnapshot;
import com.zb.fresh_api.domain.repository.reader.CategoryReader;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import com.zb.fresh_api.domain.repository.writer.ProductSnapshotWriter;
import com.zb.fresh_api.domain.repository.writer.ProductWriter;
import java.util.Objects;
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
    private final MemberReader memberReader;
    private final ProductSnapshotWriter productSnapshotWriter;

    @Transactional
    public AddProductResponse addProduct(AddProductRequest request, Member member,
        MultipartFile image) {
        if (!member.isSeller()) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_ACCESS_EXCEPTION);
        }
        Category category = categoryReader.findById(request.categoryId()).orElseThrow(
            () -> new CustomException(ResponseCode.CATEGORY_NOT_FOUND)
        );

        // TODO 1. 이미지 변환 (S3)
        final String profileImage = null;

        Product storedProduct = productWriter.store(Product.create(request, category, member, profileImage));
        return new AddProductResponse(storedProduct.getId(), storedProduct.getName(), storedProduct.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public GetProductDetailResponse getProductDetail(final GetProductDetailRequest request) {
        Product product = productReader.findById(request.productId())
            .orElseThrow(() -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND));

        return GetProductDetailResponse.fromEntity(product);
    }

    @Transactional
    public UpdateProductResponse updateProduct(final Long productId, final UpdateProductRequest request, final MultipartFile imageRequest,
        final Member loginMember) {
        Product product = productReader.findById(productId)
            .orElseThrow(() -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND));

        if(!Objects.equals(loginMember.getId(), product.getMember().getId())){
            throw  new CustomException(ResponseCode.NOT_PRODUCT_OWNER);
        }

        if(memberReader.existActiveNickname(request.name())){
            throw new CustomException(ResponseCode.NICKNAME_ALREADY_IN_USE);
        }

        productSnapshotWriter.store(ProductSnapshot.create(product));

        final String newProfileImage = convertImage(imageRequest);
        Category newCategory = findCategory(request.categoryId());
        product.update(request, newProfileImage, newCategory);


        return new UpdateProductResponse(product.getId());
    }

    // TODO 1. 이미지 변환 (S3)
    private String convertImage(MultipartFile imageRequest) {
        if (imageRequest != null && !imageRequest.isEmpty()) {
            // 이미지 변환 로직 (예: S3 업로드)
            return "newProfileImageUrl"; // 실제 변환된 이미지 URL을 반환
        }
        return null;
    }

    private Category findCategory(Long categoryId) {
        if (categoryId != null) {
            return categoryReader.findById(categoryId)
                .orElseThrow(() -> new CustomException(ResponseCode.CATEGORY_NOT_FOUND));
        }
        return null;
    }
}
