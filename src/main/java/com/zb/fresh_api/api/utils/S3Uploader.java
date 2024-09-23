package com.zb.fresh_api.api.utils;

import com.zb.fresh_api.common.constants.AppConstants;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.file.UploadedFile;
import com.zb.fresh_api.domain.enums.category.CategoryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Client s3Client;

    public UploadedFile upload(final CategoryType category, final MultipartFile file) {
        final String key = getFilePath(category, file.getOriginalFilename());
        final PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (Exception e) {
            log.error("ERROR : {} ", e.getMessage());
            throw new CustomException(ResponseCode.S3_UPLOADER_ERROR);
        }

        return new UploadedFile(key, getPublicUrl(bucket, region, key));
    }

    private String getFilePath(final CategoryType category, final String originalFilename) {
        return category.name() + File.separator + createDatePath() + File.separator + generateRandomFilePrefix() + "-" + originalFilename;
    }

    private String createDatePath() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT_YYYYMMDD);
        return now.format(dateTimeFormatter);
    }

    private Map<String, String> createMetadataFromFile(final MultipartFile file) {
        final Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        metadata.put("Content-Disposition", "inline");
        return metadata;
    }

    private String generateRandomFilePrefix() {
        String randomUUID = UUID.randomUUID().toString();
        String cleanedUUID = randomUUID.replace("-", "");
        return cleanedUUID.substring(0, 8);
    }

    private String getPublicUrl(final String bucket, final String region, final String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
    }

}
