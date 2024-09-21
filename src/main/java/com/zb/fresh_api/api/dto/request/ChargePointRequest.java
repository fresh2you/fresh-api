package com.zb.fresh_api.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ChargePointRequest (
    @NotNull
    @Min(0)
    BigDecimal point
){}
