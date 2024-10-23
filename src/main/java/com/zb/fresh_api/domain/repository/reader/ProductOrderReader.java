package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.order.ProductOrder;
import com.zb.fresh_api.domain.enums.order.ProductOrderStatus;
import com.zb.fresh_api.domain.repository.jpa.ProductOrderJpaRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor

public class ProductOrderReader {
    private final ProductOrderJpaRepository productOrderJpaRepository;

    public ProductOrder getByOrderId(String orderId, ProductOrderStatus status) {
        return productOrderJpaRepository.findByOrderIdAndProductOrderStatus(orderId, status)
            .orElseThrow(()-> new CustomException(ResponseCode.PRODUCTORDER_NOT_FOUND));
    }
}
