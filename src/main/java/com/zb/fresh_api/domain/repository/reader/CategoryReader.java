package com.zb.fresh_api.domain.repository.reader;

import com.zb.fresh_api.domain.annotation.Reader;
import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.repository.jpa.CategoryJpaRepository;
import com.zb.fresh_api.domain.repository.query.CategoryQueryRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class CategoryReader {
    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    public List<Category> findAll(){
        return categoryQueryRepository.findAll();
    }

    public Optional<Category> findById(Long id){return categoryJpaRepository.findById(id);}
}
