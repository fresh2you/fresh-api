package com.zb.fresh_api.api.dto;

import jakarta.validation.constraints.NotNull;

public record TermsAgreementDto(
    @NotNull(message = "약관 ID는 필수입니다")
    Long termsId,

    @NotNull(message = "동의 여부는 필수입니다")
    boolean isAgreed
) {}
