package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "상품 구매 응답")
public record BuyProductResponse(
    Long productOrderId,

    String productName,

    String buyerName,

    BigDecimal totalPrice
) {}
