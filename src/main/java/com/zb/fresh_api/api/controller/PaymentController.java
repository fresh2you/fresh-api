package com.zb.fresh_api.api.controller;


import com.zb.fresh_api.api.dto.request.TossPaymentsSuccess;
import com.zb.fresh_api.api.dto.response.TossPaymentSuccessResponse;
import com.zb.fresh_api.api.service.PaymentsService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "토스 페이먼츠 콜백 API",
    description = "토스 페이먼츠 연동 콜백 API"
)
@RestController
@RequestMapping("/v1/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentsService paymentsService;

    @GetMapping("/success")
    public ResponseEntity<ApiResponse<TossPaymentSuccessResponse>> successPayments(TossPaymentsSuccess request) {

        TossPaymentSuccessResponse response = paymentsService.PaymentAndUpdateProductOrder(
            request);
        return ApiResponse.success(ResponseCode.SUCCESS, response);

    }

}
