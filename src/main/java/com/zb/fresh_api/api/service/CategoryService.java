package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.response.CategoryDto;
import com.zb.fresh_api.api.dto.response.GetAllCategoryResponse;
import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.repository.reader.CategoryReader;
import java.util.List;
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
}
