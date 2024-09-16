package com.zb.fresh_api.domain.repository.jpa;

import com.zb.fresh_api.domain.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
}
