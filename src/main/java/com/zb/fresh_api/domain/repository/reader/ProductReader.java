package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.api.dto.request.GetAllProductByConditionsRequest;
import com.zb.fresh_api.api.dto.request.GetSellerProducts;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.dto.recommend.RecommendProductSummary;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.repository.jpa.ProductJpaRepository;
import com.zb.fresh_api.domain.repository.query.ProductQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Reader
@RequiredArgsConstructor
public class ProductReader {
    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryRepository productQueryRepository;

    public Optional<Product> findById(Long id){
        return productJpaRepository.findById(id);
    }

    public Page<Product> findByMemberId(GetSellerProducts request, Long memberId) {
        return productQueryRepository.findByMemberId(request, memberId);
    }

    public Product getById(Long id){
        return productJpaRepository.findById(id).orElseThrow(
            () -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND)
        );
    }

    public Page<Product> findAll(GetAllProductByConditionsRequest request){
        return productQueryRepository.findAll(request);
    }

    public List<RecommendProductSummary> getAllRandomProduct(int size) {
        return productQueryRepository.findAllRandomProduct(size);
    }

    public List<RecommendProductSummary> getAllRecommendProduct(Member member, int size) {
        return productQueryRepository.findAllRecommendProduct(member.getId(), size);
    }
}
