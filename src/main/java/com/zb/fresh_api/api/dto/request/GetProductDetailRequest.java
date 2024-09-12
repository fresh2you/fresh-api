package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record GetProductDetailRequest(
    @Schema(description = "제품 ID")
    @NotNull
    Long productId
) {}
