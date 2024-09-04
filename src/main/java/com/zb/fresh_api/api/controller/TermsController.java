package com.zb.fresh_api.api.controller;

import com.zb.fresh_api.api.dto.GetAllTermsResponse;
import com.zb.fresh_api.api.dto.GetTermsByIdResponse;
import com.zb.fresh_api.api.dto.TermsDto;
import com.zb.fresh_api.api.service.TermsService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(
    name = "약관 API",
    description = "회원가입 시 필요한 약관 관련 API"
)
@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
public class TermsController {

    private final TermsService termsService;


    @Operation(
            summary = "약관 목록 조회",
            description = "서비스 이용에 필요한 약관 목록을 조회합니다"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<GetAllTermsResponse>> getAllTerms() {
        List<TermsDto> terms = termsService.getTerms();
        return ApiResponse.success(ResponseCode.SUCCESS);
    }


    @Operation(
        summary = "약관 상세 조회",
        description = "약관의 Id를 이용해 약관을 상세 조회합니다"
    )
    @GetMapping("/{termsId}")
    public ResponseEntity<ApiResponse<GetTermsByIdResponse>> getTermsById(@PathVariable("termsId") Long termsId){
        TermsDto term = termsService.getTerm(termsId);
        return ApiResponse.success(ResponseCode.SUCCESS);
    }
}
