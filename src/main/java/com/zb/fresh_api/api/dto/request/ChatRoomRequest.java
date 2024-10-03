package com.zb.fresh_api.api.dto.request;

public record ChatRoomRequest(
        String buyerName,
        String sellerName,
        Long buyerId,
        Long sellerId,
        Long productId,
        Long categoryId
) {
}
