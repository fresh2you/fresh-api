package com.zb.fresh_api.domain.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadedFile(
        @Schema(description = "S3 key")
        String key,

        @Schema(description = "S3 url")
        String url
) {
}
