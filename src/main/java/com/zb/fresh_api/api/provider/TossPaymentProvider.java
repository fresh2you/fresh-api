package com.zb.fresh_api.api.provider;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.fresh_api.api.client.TossPaymentApiClient;
import com.zb.fresh_api.api.dto.request.TossPaymentsSuccess;
import com.zb.fresh_api.api.dto.response.TossPaymentSuccessResponse;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.PaymentResponseCode;
import feign.FeignException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TossPaymentProvider {
    private final TossPaymentApiClient tossPaymentApiClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.payment.toss.client-key}")
    private String clientKey;

    @Value("${spring.payment.toss.secret-key}")
    private String secretKey;

    @Value("${spring.payment.toss.security-key}")
    private String securityKey;

    private String setAuthorization(){
        String encodedSecretKey = Base64.getEncoder().encodeToString((secretKey+":").getBytes());
        return "Basic " + encodedSecretKey;
    }

    public TossPaymentSuccessResponse getTossPaymentSuccessResponse(TossPaymentsSuccess request){
        try {
            return tossPaymentApiClient.confirmPayment(setAuthorization(), request);
        } catch (FeignException e) {
            throw handleTossPaymentError(e);
        }
    }
    private CustomException handleTossPaymentError(FeignException e) {
        try {
            String responseBody = e.contentUTF8();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            String code = jsonResponse.get("code").asText();
//            String message = jsonResponse.get("message").asText();
            PaymentResponseCode tossError = PaymentResponseCode.fromCode(code);
            return new CustomException(tossError);
        } catch (Exception parseException) {
            return new CustomException(PaymentResponseCode.FAILED_INTERNAL_SYSTEM_PROCESSING);
        }
    }

}
