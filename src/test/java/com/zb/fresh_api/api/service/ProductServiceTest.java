package com.zb.fresh_api.api.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import com.zb.fresh_api.api.dto.request.AddProductRequest;
import com.zb.fresh_api.api.dto.response.AddProductResponse;
import com.zb.fresh_api.common.base.ServiceTest;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.reader.CategoryReader;
import com.zb.fresh_api.domain.repository.writer.ProductWriter;
import java.math.BigDecimal;
import java.util.Optional;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("제품 관련 비즈니스 테스트")
class ProductServiceTest extends ServiceTest {

    @Mock
    private ProductWriter productWriter;

    @Mock
    private CategoryReader categoryReader;

    @InjectMocks
    private ProductService productService;

    @DisplayName("제품 등록 성공")
    @RepeatedTest(value = 10, name = RepeatedTest.LONG_DISPLAY_NAME)

    void addProduct_success() {
        // given
        Long categoryId = Arbitraries.longs().greaterOrEqual(1L).sample();
        AddProductRequest addProductRequest = getConstructorMonkey().giveMeBuilder(
                AddProductRequest.class)
            .set("name", Arbitraries.strings().ofMinLength(1))
            .set("quantity", Arbitraries.integers().greaterOrEqual(1))
            .set("description", Arbitraries.strings().ofMinLength(1).ofMaxLength(250))
            .set("price", Arbitraries.bigDecimals().greaterOrEqual(BigDecimal.valueOf(1)))
            .set("categoryId", categoryId)
            .sample();
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
            .set("id", Arbitraries.longs().greaterOrEqual(4L))
            .set("provider", Arbitraries.of(Provider.class))
            .set("providerId",
                Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
            .set("isSeller", true)
            .set("email" , Arbitraries.strings().withCharRange('a','z') + "@test.com")
            .sample();
        Category category = getConstructorMonkey().giveMeBuilder(Category.class)
            .set("id", addProductRequest.categoryId())
            .set("name", Arbitraries.strings().ofMinLength(1))
            .sample();
        Product product = getConstructorMonkey().giveMeBuilder(Product.class)
            .set("member", member)
            .set("category", category)
            .set("name" , addProductRequest.name())
            .sample();

        doReturn(Optional.of(category)).when(categoryReader)
            .findById(addProductRequest.categoryId());
        doReturn(product).when(productWriter).store(argThat(x ->
                x.getName().equals(addProductRequest.name()) &&
                    x.getMember().getEmail().equals(member.getEmail()) &&
                    x.getCategory().getName().equals(category.getName())
            )

        );
        // When
        AddProductResponse response = productService.addProduct(addProductRequest,
            member);

        // then
        assertNotNull(response);
        assertEquals(addProductRequest.name(), response.name());
        verify(categoryReader).findById(categoryId);
        verify(productWriter).store(any(Product.class));
    }

    @DisplayName("제품 등록 실패 - 사용자가 판매자가 아님")
    @RepeatedTest(value = 10, name = RepeatedTest.LONG_DISPLAY_NAME)

    void addProduct_fail_UNAUTHORIZED() {
        // given
        AddProductRequest addProductRequest = getConstructorMonkey().giveMeBuilder(
                AddProductRequest.class)
            .set("name", Arbitraries.strings().ofMinLength(1))
            .set("quantity", Arbitraries.integers().greaterOrEqual(1))
            .set("description", Arbitraries.strings().ofMinLength(1).ofMaxLength(250))
            .set("price", Arbitraries.bigDecimals().greaterOrEqual(BigDecimal.valueOf(1)))
            .set("categoryId", Arbitraries.longs().greaterOrEqual(1L))
            .sample();
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
            .set("id", Arbitraries.longs().greaterOrEqual(4L))
            .set("provider", Arbitraries.of(Provider.class))
            .set("providerId",
                Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
            .set("isSeller", false)
            .sample();

        // When
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(addProductRequest, member));

        // then
        assertEquals(ResponseCode.UNAUTHORIZED_ACCESS_EXCEPTION, exception.getResponseCode());
    }

    @DisplayName("제품 등록 실패 - 카테고리 ID가 유효하지 않음")
//    @Test
    @RepeatedTest(value = 10, name = RepeatedTest.LONG_DISPLAY_NAME)
    void addProduct_fail_INVALID_CATEGORYID() {
        // given
        AddProductRequest addProductRequest = getConstructorMonkey().giveMeBuilder(
                AddProductRequest.class)
            .set("name", Arbitraries.strings().ofMinLength(1))
            .set("quantity", Arbitraries.integers().greaterOrEqual(1))
            .set("description", Arbitraries.strings().ofMinLength(1).ofMaxLength(250))
            .set("price", Arbitraries.bigDecimals().greaterOrEqual(BigDecimal.valueOf(1)))
            .set("categoryId", Arbitraries.longs().greaterOrEqual(1L))
            .sample();
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
            .set("id", Arbitraries.longs().greaterOrEqual(4L))
            .set("provider", Arbitraries.of(Provider.class))
            .set("providerId",
                Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
            .set("isSeller", true)
            .sample();

        doReturn(Optional.empty()).when(categoryReader).findById(addProductRequest.categoryId());

        // When
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(addProductRequest, member));

        // then
        assertEquals(ResponseCode.CATEGORY_NOT_VALID, exception.getResponseCode());
    }
}
