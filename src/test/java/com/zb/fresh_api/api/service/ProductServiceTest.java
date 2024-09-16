package com.zb.fresh_api.api.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.zb.fresh_api.api.dto.request.AddProductRequest;
import com.zb.fresh_api.api.dto.request.GetProductDetailRequest;
import com.zb.fresh_api.api.dto.response.AddProductResponse;
import com.zb.fresh_api.api.dto.response.GetProductDetailResponse;
import com.zb.fresh_api.common.base.ServiceTest;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.reader.CategoryReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import com.zb.fresh_api.domain.repository.writer.ProductWriter;
import java.math.BigDecimal;
import java.util.Optional;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("제품 관련 비즈니스 테스트")
class ProductServiceTest extends ServiceTest {

    @Mock
    private ProductWriter productWriter;

    @Mock
    private CategoryReader categoryReader;

    @Mock
    private  ProductReader productReader;

    @InjectMocks
    private ProductService productService;

    @DisplayName("제품 등록 성공")
    @Test
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
        MultipartFile multipartFile = mock(MultipartFile.class);

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
            member, multipartFile);

        // then
        assertNotNull(response);
        assertEquals(addProductRequest.name(), response.name());
        verify(categoryReader).findById(categoryId);
        verify(productWriter).store(any(Product.class));
    }

    @DisplayName("제품 등록 실패 - 사용자가 판매자가 아님")
    @Test
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

        MultipartFile multipartFile = mock(MultipartFile.class);

        // When
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(addProductRequest, member,
            multipartFile));

        // then
        assertEquals(ResponseCode.UNAUTHORIZED_ACCESS_EXCEPTION, exception.getResponseCode());
    }

    @DisplayName("제품 등록 실패 - 카테고리 ID가 유효하지 않음")
    @Test
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
        MultipartFile multipartFile = mock(MultipartFile.class);

        doReturn(Optional.empty()).when(categoryReader).findById(addProductRequest.categoryId());

        // When
        CustomException exception = assertThrows(CustomException.class, () -> productService.addProduct(addProductRequest, member,
            multipartFile));

        // then
        assertEquals(ResponseCode.CATEGORY_NOT_FOUND, exception.getResponseCode());
    }

    @Test
    @DisplayName("상품 상세 조회 실패 - 상품 id가 유효하지않음")
    void getProduct_fail_PRODUCTID_NOT_FOUND() {
        // given
        Long productId = Arbitraries.longs().greaterOrEqual(1L).sample();
        GetProductDetailRequest request = getConstructorMonkey().giveMeBuilder(
                GetProductDetailRequest.class).set("productId", productId)
            .sample();

        doReturn(Optional.empty()).when(productReader).findById(request.productId());

        // when
        CustomException customException = assertThrows(CustomException.class,
            () -> productService.getProductDetail(request));

        // then
        assertEquals(customException.getResponseCode(), ResponseCode.PRODUCT_NOT_FOUND);
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void getProductDetail_success(){
        // given
        Long productId = Arbitraries.longs().greaterOrEqual(1L).sample();
        GetProductDetailRequest request = getConstructorMonkey().giveMeBuilder(
                GetProductDetailRequest.class).set("productId", productId)
            .sample();
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
            .set("name" , Arbitraries.strings())
            .sample();
        Product product = getConstructorMonkey().giveMeBuilder(Product.class)
            .set("id", productId)
            .set("name" , Arbitraries.strings().withCharRange('a', 'z'))
            .set("member", member)
            .set("price" , Arbitraries.bigDecimals())
            .set("quantity", Arbitraries.integers().greaterOrEqual(1))
            .set("description" , Arbitraries.strings().ofMinLength(1))
            .set("productImage", Arbitraries.strings().ofMinLength(1))
            .sample();

        doReturn(Optional.of(product)).when(productReader).findById(request.productId());

        // when
        GetProductDetailResponse productDetail = productService.getProductDetail(request);

        // then
        assertEquals(product.getName(), productDetail.productName());
        assertEquals(product.getId(), productDetail.productId());
        assertEquals(product.getPrice(), productDetail.price());
        assertEquals(product.getQuantity(), productDetail.quantity());
        assertEquals(product.getDescription(), productDetail.description());
        assertEquals(product.getProductImage(), productDetail.imageUrl());
    }
}
