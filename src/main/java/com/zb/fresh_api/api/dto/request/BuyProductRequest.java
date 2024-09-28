package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record BuyProductRequest(
    @Schema(description = "배송정보 ID")
    @NotNull
    Long deliveryAddressId,

    @Schema(description = "수량")
    @NotNull
    int quantity

) {}
