package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record PrepareTossPaymentResponse(

    @Schema(description = "Order ID")
    String orderId,

    @Schema(description = "사용자 ID")
    Long userId,

    @Schema(description = "구매 수량")
    int quantity,

    @Schema(description = "상풍명")
    String productName,

    @Schema(description = "결제금액")
    BigDecimal totalPrice

) {

}
