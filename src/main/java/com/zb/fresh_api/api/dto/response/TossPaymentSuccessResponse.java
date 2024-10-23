package com.zb.fresh_api.api.dto.response;

public record TossPaymentSuccessResponse(
    String mId,
    String lastTransactionKey,
    String paymentKey,
    String orderId,
    String orderName,
    int taxExemptionAmount,
    String status,
    String requestedAt,
    String approvedAt,
    boolean useEscrow,
    boolean cultureExpense,
    CardInfo card,
    EasyPay easyPay,
    int easyPayAmount,
    int easyPayDiscountAmount,
    String country,
    boolean isPartialCancelable,
    Receipt receipt,
    Checkout checkout,
    String currency,
    int totalAmount,
    int balanceAmount,
    int suppliedAmount,
    int vat,
    int taxFreeAmount,
    String method,
    String version
) {

}

record CardInfo(
    String issuerCode,
    String acquirerCode,
    String number,
    int installmentPlanMonths,
    boolean isInterestFree,
    String interestPayer,
    String approveNo,
    boolean useCardPoint,
    String cardType,
    String ownerType,
    String acquireStatus,
    String receiptUrl,
    int amount
) {

}

record EasyPay(
    String provider,
    int amount,
    int discountAmount
) {

}

record Receipt(
    String url
) {

}

record Checkout(
    String url
) {

}
