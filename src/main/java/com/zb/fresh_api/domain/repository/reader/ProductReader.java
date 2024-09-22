package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.api.dto.request.GetAllProductByConditionsRequest;
import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.repository.jpa.ProductJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@Reader
@RequiredArgsConstructor
public class ProductReader {
    private final ProductJpaRepository productJpaRepository;

    public Optional<Product> findById(Long id){
        return productJpaRepository.findById(id);
    }

    public Product getById(Long id){
        return productJpaRepository.findById(id).orElseThrow(
            () -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND)
        );
    }

    public Page<Product> findAll(GetAllProductByConditionsRequest request){
        return productQueryRepository.findAll(request);
    }
}
