package com.zb.fresh_api.domain.enums.order;

public enum ProductOrderStatus {
    // 결제 요청 상태
    REQUESTED,  // 결제 요청됨

    // 결제 승인 상태
    APPROVED,   // 결제 승인됨
    FAILED      // 결제 실패
}
