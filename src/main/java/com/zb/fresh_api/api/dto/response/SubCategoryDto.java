package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.category.Category;

public record SubCategoryDto (Long categoryId, String categoryName){
    public static SubCategoryDto fromCategory(Category category) {
        return new SubCategoryDto(category.getId(), category.getName());
    }
}
