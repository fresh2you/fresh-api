package com.zb.fresh_api.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ChargePointResponse(
    @Schema(description = "충전액")
    BigDecimal chargePoint,

    @Schema(description = "총액")
    BigDecimal balance
) {

}
