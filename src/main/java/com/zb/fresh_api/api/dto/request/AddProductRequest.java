package com.zb.fresh_api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "제품 등록 요청")
public record AddProductRequest(

    @NotEmpty(message = "제품 이름이 누락 되었습니다.")
    String name,

    @NotNull(message = "재고가 누락 되었습니다.")
    int quantity,

    @NotEmpty(message = "제품 설명이 누락되었습니다.")
    String description,

    @NotNull(message = "가격이 누락되었습니다.")
    BigDecimal price,

    @NotNull
    Long categoryId
) {}
