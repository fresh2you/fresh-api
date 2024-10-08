package com.zb.fresh_api.api.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.zb.fresh_api.api.dto.request.AddProductRequest;
import com.zb.fresh_api.api.dto.request.GetAllProductByConditionsRequest;
import com.zb.fresh_api.api.dto.response.FindAllProductLikeResponse;
import com.zb.fresh_api.api.dto.response.GetAllProductByConditionsResponse;
import com.zb.fresh_api.api.dto.request.BuyProductRequest;
import com.zb.fresh_api.api.dto.request.GetAllProductByConditionsRequest;
import com.zb.fresh_api.api.dto.response.BuyProductResponse;
import com.zb.fresh_api.api.dto.response.FindAllProductLikeResponse;
import com.zb.fresh_api.api.dto.response.GetAllProductByConditionsResponse;
import com.zb.fresh_api.api.dto.request.GetProductDetailRequest;
import com.zb.fresh_api.api.dto.response.AddProductResponse;
import com.zb.fresh_api.api.dto.response.BuyProductResponse;
import com.zb.fresh_api.api.dto.response.GetProductDetailResponse;
import com.zb.fresh_api.api.dto.response.LikeProductResponse;
import com.zb.fresh_api.api.utils.S3Uploader;
import com.zb.fresh_api.common.base.ServiceTest;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.address.DeliveryAddress;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.point.Point;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.entity.product.ProductLike;
import com.zb.fresh_api.domain.enums.member.Provider;
import com.zb.fresh_api.domain.repository.reader.CategoryReader;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.ProductLikeReader;
import com.zb.fresh_api.domain.repository.reader.DeliveryAddressReader;
import com.zb.fresh_api.domain.repository.reader.MemberReader;
import com.zb.fresh_api.domain.repository.reader.PointReader;
import com.zb.fresh_api.domain.repository.reader.ProductLikeReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import com.zb.fresh_api.domain.repository.writer.DeliveryAddressSnapshotWriter;
import com.zb.fresh_api.domain.repository.writer.PointHistoryWriter;
import com.zb.fresh_api.domain.repository.writer.ProductLikeWriter;
import com.zb.fresh_api.domain.repository.writer.ProductOrderWriter;
import com.zb.fresh_api.domain.repository.writer.ProductSnapshotWriter;
import com.zb.fresh_api.domain.repository.writer.ProductWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("제품 관련 비즈니스 테스트")
class ProductServiceTest extends ServiceTest {

    @Mock
    private ProductWriter productWriter;

    @Mock
    private CategoryReader categoryReader;

    @Mock
    private  ProductReader productReader;
    @Mock
    private  ProductSnapshotWriter productSnapshotWriter;
    @Mock
    private  MemberReader memberReader;
    @Mock
    private  DeliveryAddressReader deliveryAddressReader;
    @Mock
    private  PointReader pointReader;
    @Mock
    private  PointHistoryWriter pointHistoryWriter;
    @Mock
    private  DeliveryAddressSnapshotWriter deliveryAddressSnapshotWriter;
    @Mock
    private  ProductOrderWriter productOrderWriter;

    @Mock
    private ProductLikeReader productLikeReader;


    @Mock
    private S3Uploader s3Uploader;

    @Mock
    private  ProductLikeWriter productLikeWriter;
    @InjectMocks
    private ProductService productService;

//    @DisplayName("제품 등록 성공")
//    @Test
//    void addProduct_success() {
//        // given
//        Long categoryId = Arbitraries.longs().greaterOrEqual(1L).sample();
//        AddProductRequest addProductRequest = getConstructorMonkey().giveMeBuilder(
//                AddProductRequest.class)
//            .set("name", Arbitraries.strings().ofMinLength(1))
//            .set("quantity", Arbitraries.integers().greaterOrEqual(1))
//            .set("description", Arbitraries.strings().ofMinLength(1).ofMaxLength(250))
//            .set("price", Arbitraries.bigDecimals().greaterOrEqual(BigDecimal.valueOf(1)))
//            .set("categoryId", categoryId)
//            .sample();
//        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
//            .set("id", Arbitraries.longs().greaterOrEqual(4L))
//            .set("provider", Arbitraries.of(Provider.class))
//            .set("providerId",
//                Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
//            .set("isSeller", true)
//            .set("email" , Arbitraries.strings().withCharRange('a','z') + "@test.com")
//            .sample();
//        Category category = getConstructorMonkey().giveMeBuilder(Category.class)
//            .set("id", addProductRequest.categoryId())
//            .set("name", Arbitraries.strings().ofMinLength(1))
//            .sample();
//        Product product = getConstructorMonkey().giveMeBuilder(Product.class)
//            .set("member", member)
//            .set("category", category)
//            .set("name" , addProductRequest.name())
//            .sample();
//        MultipartFile multipartFile = mock(MultipartFile.class);
//        String imageUrl = Arbitraries.strings().withCharRange('a', 'z').sample();
//        UploadedFile uploadedFile = getConstructorMonkey().giveMeBuilder(UploadedFile.class)
//            .set("key", Arbitraries.strings().withCharRange('a', 'z')).set("url", imageUrl)
//            .sample();
//        doReturn(Optional.of(category)).when(categoryReader)
//            .findById(addProductRequest.categoryId());
//        doReturn(uploadedFile).when(s3Uploader).upload(CategoryType.PRODUCT, multipartFile);
//        doReturn(product).when(productWriter).store(argThat(x ->
//                x.getName().equals(addProductRequest.name()) &&
//                    x.getMember().getEmail().equals(member.getEmail()) &&
//                    x.getCategory().getName().equals(category.getName())
//            )
//
//        );
//        // When
//        AddProductResponse response = productService.addProduct(addProductRequest,
//            member, multipartFile);
//
//        // then
//        assertNotNull(response);
//        assertEquals(addProductRequest.name(), response.name());
//        verify(categoryReader).findById(categoryId);
//        verify(productWriter).store(any(Product.class));
//    }

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


        doReturn(Optional.empty()).when(productReader).findById(productId);

        // when
        CustomException customException = assertThrows(CustomException.class,
            () -> productService.getProductDetail(productId));

        // then
        assertEquals(customException.getResponseCode(), ResponseCode.PRODUCT_NOT_FOUND);
    }

    @Test
    @DisplayName("상품 상세 조회 성공")
    void getProductDetail_success(){
        // given
        Long productId = Arbitraries.longs().greaterOrEqual(1L).sample();
        Member member = getReflectionMonkey().giveMeBuilder(Member.class)
            .set("name" , Arbitraries.strings())
            .sample();
        Product product = getReflectionMonkey().giveMeBuilder(Product.class)
            .set("id", productId)
            .set("name" , Arbitraries.strings().withCharRange('a', 'z'))
            .set("member", member)
            .set("price" , Arbitraries.bigDecimals())
            .set("quantity", Arbitraries.integers().greaterOrEqual(1))
            .set("description" , Arbitraries.strings().ofMinLength(1))
            .set("productImage", Arbitraries.strings().ofMinLength(1))
            .sample();

        doReturn(Optional.of(product)).when(productReader).findById(productId);

        // when
        GetProductDetailResponse productDetail = productService.getProductDetail(productId);

        // then
        assertEquals(product.getName(), productDetail.productName());
        assertEquals(product.getId(), productDetail.productId());
        assertEquals(product.getPrice(), productDetail.price());
        assertEquals(product.getQuantity(), productDetail.quantity());
        assertEquals(product.getDescription(), productDetail.description());
        assertEquals(product.getProductImage(), productDetail.imageUrl());
    }

    @Test
    @DisplayName("상품 목록 조회 성공")
    void findProducts_success(){
        // given
        GetAllProductByConditionsRequest request =
            getConstructorMonkey().giveMeBuilder(GetAllProductByConditionsRequest.class)
                .set("categoryId", Arbitraries.longs().greaterOrEqual(1L))
                .set("keyword", Arbitraries.strings().ofMaxLength(10))
                .set("page",Arbitraries.integers().greaterOrEqual(1).lessOrEqual(3))
                .set("size",Arbitraries.integers().greaterOrEqual(1).lessOrEqual(3))
                .sample();
        List<Member> memberList = getReflectionMonkey().giveMe(Member.class, 3);
        Product product1 = getReflectionMonkey().giveMeBuilder(Product.class)
            .set("member", memberList.get(0))
            .sample();
        Product product2 = getReflectionMonkey().giveMeBuilder(Product.class)
            .set("member", memberList.get(1))
            .sample();
        Product product3 = getReflectionMonkey().giveMeBuilder(Product.class)
            .set("member", memberList.get(2))
            .sample();
        List<Product> productList = new ArrayList<>(List.of(product1,product2,product3));


        Pageable pageable = PageRequest.of(0, 3);
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        doReturn(productPage).when(productReader).findAll(request);

        // when
        GetAllProductByConditionsResponse products = productService.findProducts(request);

        assertNotNull(products);
        assertEquals(3, products.productList().size());
    }

    @Test
    @DisplayName("좋아요한 상품 목록 조회 성공")
    void findAllProductLike_success(){
        // given
        Long memberId = Arbitraries.longs().greaterOrEqual(1L).sample();
        Long product1Id = Arbitraries.longs().greaterOrEqual(1L).sample();
        Long product2Id = Arbitraries.longs().greaterOrEqual(1L).sample();
        Long product3Id = Arbitraries.longs().greaterOrEqual(1L).sample();
        List<Long> productLikeIds = new ArrayList<>(List.of(product1Id,product2Id,product3Id));
        List<Product> products = new ArrayList<>(List.of(
            getReflectionMonkey().giveMeBuilder(Product.class)
                .set("id", product1Id).sample(),
            getReflectionMonkey().giveMeBuilder(Product.class)
                .set("id", product2Id).sample(),
            getReflectionMonkey().giveMeBuilder(Product.class)
                .set("id", product3Id).sample()));

        doReturn(productLikeIds).when(productLikeReader).findProductIdByMemberId(memberId);
        doReturn(Optional.of(products.get(0))).when(productReader).findById(product1Id);
        doReturn(Optional.of(products.get(1))).when(productReader).findById(product2Id);
        doReturn(Optional.of(products.get(2))).when(productReader).findById(product3Id);


        // when
        FindAllProductLikeResponse response = productService.findAllProductLike(memberId);

        // then
        assertEquals(response.productList().size(), productLikeIds.size());
        assertNotNull(response);
    }

    @Test
    @DisplayName("상품 좋아요 성공")
    void likeProduct_success() {
        // given
        Long memberId = Arbitraries.longs().greaterOrEqual(1L).sample();
        Long productId = Arbitraries.longs().greaterOrEqual(1L).sample();
        Product product = getReflectionMonkey().giveMeBuilder(Product.class)
            .set("id", productId).sample();
        Member member = getReflectionMonkey().giveMeBuilder(Member.class)
            .set("id", memberId).sample();
        ProductLike productLike = ProductLike.create(member, product);

        doReturn(Optional.of(product)).when(productReader).findById(productId);
        doReturn(member).when(memberReader).getById(memberId);
        doReturn(false).when(productLikeReader).isExist(productId, memberId);
        doReturn(productLike).when(productLikeWriter).store(argThat(
            m -> m.getProduct().equals(product) &&
                m.getMember().equals(member)
        ));

        // when
        LikeProductResponse response = productService.like(productId, memberId);

        // then
        assertNotNull(response);
        assertEquals(response.productId(), productId);
    }

    @Test
    @DisplayName("상품 좋아요 취소 성공")
    void unLikeProduct_success() {
        // given
        Long memberId = Arbitraries.longs().greaterOrEqual(1L).sample();
        Long productId = Arbitraries.longs().greaterOrEqual(1L).sample();
        Product product = getReflectionMonkey().giveMeBuilder(Product.class)
            .set("id", productId).sample();
        Member member = getReflectionMonkey().giveMeBuilder(Member.class)
            .set("id", memberId).sample();
        ProductLike productLike = ProductLike.create(member, product);

        doReturn(Optional.of(product)).when(productReader).findById(productId);
        doReturn(member).when(memberReader).getById(memberId);
        doReturn(Optional.of(productLike)).when(productLikeReader).findByProductIdAndMemberId(productId, memberId);

        // when
        productService.unLike(productId, memberId);

        // then
        verify(productLikeWriter, times(1)).delete(productLike);
    }


    @Test
    @DisplayName("제품 구매 성공")
    void buyProduct_success() {
        // given
        Long productId = Arbitraries.longs().greaterOrEqual(1L).sample();
        Long memberId = Arbitraries.longs().greaterOrEqual(1L).sample();
        Long deliveryAddressId =  Arbitraries.longs().greaterOrEqual(1L).sample();
        BigDecimal productPrice = Arbitraries.bigDecimals().greaterOrEqual(BigDecimal.valueOf(10)).lessOrEqual(
            BigDecimal.valueOf(100)
        ).sample();
        int quantity = Arbitraries.integers().greaterOrEqual(1).lessOrEqual(10).sample();
        BuyProductRequest request = getConstructorMonkey().giveMeBuilder(BuyProductRequest.class)
            .set("quantity", quantity)
            .set("deliveryAddressId",deliveryAddressId)
            .sample();

        BigDecimal memberBalance = productPrice.multiply(BigDecimal.valueOf(request.quantity())).add(BigDecimal.valueOf(1));
        Product product = getReflectionMonkey().giveMeBuilder(Product.class)
            .set("id", productId)
            .set("price", productPrice)
            .set("quantity", quantity)
            .sample();
        Member member = getConstructorMonkey().giveMeBuilder(Member.class)
            .set("id", memberId)
            .sample();
        DeliveryAddress deliveryAddress = getReflectionMonkey().giveMeBuilder(DeliveryAddress.class)
            .set("id", request.deliveryAddressId())
            .set("memberId", memberId)
            .sample();
        Point memberPoint = getConstructorMonkey().giveMeBuilder(Point.class)
            .set("balance", memberBalance)
            .sample();

        doReturn(product).when(productReader).getById(productId);
        doReturn(member).when(memberReader).getById(memberId);
        doReturn(deliveryAddress).when(deliveryAddressReader).getActiveDeliveryAddressByIdAndMemberId(request.deliveryAddressId(), memberId);
        doReturn(memberPoint).when(pointReader).getByMemberId(memberId);

        // When
        BuyProductResponse response = productService.buyProduct(productId, request, memberId);

        // then
        assertNotNull(response);
        assertEquals(product.getName(), response.productName());
        assertEquals(deliveryAddress.getRecipientName(), response.buyerName());
        assertEquals(product.getPrice().multiply(BigDecimal.valueOf(request.quantity())), response.totalPrice());
        verify(productSnapshotWriter, times(1)).store(any());
        verify(deliveryAddressSnapshotWriter, times(1)).store(any());
        verify(productOrderWriter, times(1)).store(any());
    }


}
