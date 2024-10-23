package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.request.TossPaymentsSuccess;
import com.zb.fresh_api.api.dto.response.TossPaymentSuccessResponse;
import com.zb.fresh_api.api.provider.TossPaymentProvider;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.order.ProductOrder;
import com.zb.fresh_api.domain.entity.point.Point;
import com.zb.fresh_api.domain.entity.point.PointHistory;
import com.zb.fresh_api.domain.enums.order.ProductOrderStatus;
import com.zb.fresh_api.domain.enums.point.PointTransactionType;
import com.zb.fresh_api.domain.repository.reader.PointReader;
import com.zb.fresh_api.domain.repository.reader.ProductOrderReader;
import com.zb.fresh_api.domain.repository.writer.PointHistoryWriter;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final ProductOrderReader productOrderReader;
    private final TossPaymentProvider tossPaymentProvider;
    private final PointReader pointReader;
    private final PointHistoryWriter pointHistoryWriter;

    @Transactional
    public TossPaymentSuccessResponse PaymentAndUpdateProductOrder(final TossPaymentsSuccess request) {
        ProductOrder productOrder = validateSuccessRequestAndGetProductOrder(request);
        try{
            TossPaymentSuccessResponse tossPaymentConfirmResponse = tossPaymentProvider.getTossPaymentSuccessResponse(
                request);
            processSuccessfulPayment(productOrder);
            return tossPaymentConfirmResponse;
        }catch (CustomException e) {
            // 4. 결제 실패 시 기존 ProductOrder 상태변경해야함
            productOrder.paymentFail();
            throw e;
        }
    }

    private ProductOrder validateSuccessRequestAndGetProductOrder(final TossPaymentsSuccess request){
        ProductOrder productOrder = productOrderReader.getByOrderId(request.orderId(), ProductOrderStatus.REQUESTED);
        if(request.amount() != productOrder.getQuantity()){
            throw new CustomException(ResponseCode.AMOUNT_NOT_CORRECT);
        }
        return productOrder;
    }

    @Transactional
    protected void processSuccessfulPayment(ProductOrder productOrder) {
        Member member = productOrder.getDeliveryAddressSnapshot().getDeliveryAddress().getMember();
        Point memberPoint = pointReader.getByMemberId(member.getId());
        BigDecimal totalPrice = productOrder.getProductSnapshot().getProduct().getPrice()
            .multiply(BigDecimal.valueOf(productOrder.getQuantity()));

        // 포인트 차감 이력 생성
        PointHistory pointHistory = PointHistory.create(memberPoint, PointTransactionType.PAYMENT,
            totalPrice, memberPoint.getBalance(), memberPoint.getBalance().subtract(totalPrice),
            "상품 구매");
        pointHistoryWriter.store(pointHistory);

        // 포인트 차감
        memberPoint.pay(totalPrice);

        // 주문 상태 업데이트
        productOrder.paymentSuccess();
    }

}
