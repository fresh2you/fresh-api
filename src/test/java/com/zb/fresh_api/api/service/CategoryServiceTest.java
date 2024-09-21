package com.zb.fresh_api.api.service;

import com.zb.fresh_api.common.base.ServiceTest;
import com.zb.fresh_api.domain.repository.reader.CategoryReader;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("카테고리 비즈니스 테스트")
class CategoryServiceTest extends ServiceTest {
    @Mock
    private CategoryReader categoryReader;

    @InjectMocks
    private CategoryService categoryService;

//    @Test
//    @DisplayName("카테고리 목록 조회성공")
//    void getAllCategories_success(){
//        // given
//        List<Category> categoryList = new ArrayList<>(List.of(
//            getConstructorMonkey().giveMeBuilder(Category.class)
//                .set("id", Arbitraries.longs().greaterOrEqual(1L))
//                .set("is_used", 1)
//                .set("name", Arbitraries.strings().ofMinLength(1).ofMaxLength(20))
//                .sample(),
//            getConstructorMonkey().giveMeBuilder(Category.class)
//                .set("id", Arbitraries.longs().greaterOrEqual(1L))
//                .set("is_used", 1)
//                .set("name", Arbitraries.strings().ofMinLength(1).ofMaxLength(20))
//                .sample(),
//            getConstructorMonkey().giveMeBuilder(Category.class)
//                .set("id", Arbitraries.longs().greaterOrEqual(1L))
//                .set("is_used", 1)
//                .set("name", Arbitraries.strings().ofMinLength(1).ofMaxLength(20))
//                .sample()
//        ));
//        doReturn(categoryList).when(categoryReader).findAll();
//
//        // when
//        GetAllCategoryResponse response = categoryService.getAllCategories();
//
//        // then
//        assertNotNull(response);
//        assertEquals(categoryList.size(), response.categories().size());
//    }
}
