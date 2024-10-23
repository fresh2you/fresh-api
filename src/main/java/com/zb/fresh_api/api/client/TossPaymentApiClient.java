package com.zb.fresh_api.api.client;

import com.zb.fresh_api.api.dto.request.TossPaymentsSuccess;
import com.zb.fresh_api.api.dto.response.TossPaymentSuccessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "toss-payment", url = "${spring.payment.toss.url}")
public interface TossPaymentApiClient {

    @PostMapping(value = "/confirm")
    TossPaymentSuccessResponse confirmPayment(
        @RequestHeader("Authorization") String authorization,
        @RequestBody TossPaymentsSuccess request);
}
