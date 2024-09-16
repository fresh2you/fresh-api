package com.zb.fresh_api.api.controller;


import com.zb.fresh_api.api.dto.response.GetAllCategoryResponse;
import com.zb.fresh_api.api.service.CategoryService;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
    name = "카테고리 API",
    description = "카테고리 관련 API"
)
@RestController
@RequestMapping("/v1/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private  final CategoryService categoryService;

    @Operation(
        summary = "",
        description = "카테고리 선택 시 사용할 API 입니다"
    )
    @GetMapping()
    public ResponseEntity<ApiResponse<GetAllCategoryResponse>> getAllCategories() {
        GetAllCategoryResponse response = categoryService.getAllCategories();
        return ApiResponse.success(ResponseCode.SUCCESS,response);
    }
}
