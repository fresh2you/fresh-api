package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.response.CategoryDto;
import com.zb.fresh_api.api.dto.response.GetAllCategoryResponse;
import com.zb.fresh_api.api.dto.response.SubCategoryDto;
import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.repository.reader.CategoryReader;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryReader categoryReader;

    public GetAllCategoryResponse getAllCategories() {
        List<Category> categories = categoryReader.findAll();
        List<CategoryDto> categoryDTOs = CategoryDto.fromCategories(categories);

        return new GetAllCategoryResponse(categoryDTOs);
    }

    private List<Category> getParentCategories(List<Category> categories) {
        return categories.stream()
            .filter(category -> category.getParent() == null) // 상위 카테고리만 필터링
            .collect(Collectors.toList());
    }

    private List<SubCategoryDto> getSubCategories(List<Category> categories, Long parentId) {
        return categories.stream()
            .filter(category -> category.getParent() != null && category.getParent().getId().equals(parentId))
            .map(subCategory -> new SubCategoryDto(subCategory.getId(), subCategory.getName()))
            .collect(Collectors.toList());
    }
}
