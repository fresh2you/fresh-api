package com.zb.fresh_api.api.dto.request;

public record TossPaymentsSuccess(
    String paymentKey,
    String orderId,
    Long amount
) {

}
