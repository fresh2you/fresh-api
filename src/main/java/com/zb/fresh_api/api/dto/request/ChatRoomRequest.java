package com.zb.fresh_api.api.dto.request;

public record ChatRoomRequest(
        Long buyerId,
        Long sellerId,
        Long productId,
        Long categoryId
) {
}
