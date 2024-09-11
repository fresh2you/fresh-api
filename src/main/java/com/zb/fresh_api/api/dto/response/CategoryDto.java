package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.category.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

public record CategoryDto (
    @Schema(description = "카테고리 ID")
    Long categoryId,
    @Schema(description = "카테고리 이름")
    String categoryName,
    @Schema(description = "하위 카테고리 목록")
    List<SubCategoryDto> subCategories) {
    public static CategoryDto fromCategory(Category category, List<SubCategoryDto> subCategories) {
        return new CategoryDto(
            category.getId(),
            category.getName(),
            subCategories
        );
    }

    public static List<CategoryDto> fromCategories(List<Category> categories) {
        return getParentCategories(categories).stream()
            .map(parentCategory -> {
                List<SubCategoryDto> subCategories = getSubCategories(categories, parentCategory.getId());
                return fromCategory(parentCategory, subCategories);
            })
            .collect(Collectors.toList());
    }
    private static List<Category> getParentCategories(List<Category> categories) {
        return categories.stream()
            .filter(category -> category.getParent() == null) // 상위 카테고리만 필터링
            .collect(Collectors.toList());
    }

    private static List<SubCategoryDto> getSubCategories(List<Category> categories, Long parentId) {
        return categories.stream()
            .filter(category -> category.getParent() != null && category.getParent().getId().equals(parentId))
            .map(subCategory -> new SubCategoryDto(subCategory.getId(), subCategory.getName()))
            .collect(Collectors.toList());
    }
}
