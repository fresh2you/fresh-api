package com.zb.fresh_api.domain.dto.file;

import com.zb.fresh_api.domain.enums.file.FileType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public record UploadFile(
        @Schema(description = "파일 타입", example = "PROFILE_IMAGE, POST_IMAGE")
        FileType fileType,

        @Schema(description = "파일")
        MultipartFile file
) {
}
