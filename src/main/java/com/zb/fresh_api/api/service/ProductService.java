package com.zb.fresh_api.api.service;

import com.zb.fresh_api.api.dto.request.AddProductRequest;
import com.zb.fresh_api.api.dto.request.BuyProductRequest;
import com.zb.fresh_api.api.dto.request.DeleteProductRequest;
import com.zb.fresh_api.api.dto.request.GetAllProductByConditionsRequest;
import com.zb.fresh_api.api.dto.request.UpdateProductRequest;
import com.zb.fresh_api.api.dto.response.AddProductResponse;
import com.zb.fresh_api.api.dto.response.BuyProductResponse;
import com.zb.fresh_api.api.dto.response.DeleteProductResponse;
import com.zb.fresh_api.api.dto.response.FindAllProductLikeResponse;
import com.zb.fresh_api.api.dto.response.GetAllProductByConditionsResponse;
import com.zb.fresh_api.api.dto.response.GetProductDetailResponse;
import com.zb.fresh_api.api.dto.response.LikeProductResponse;
import com.zb.fresh_api.api.dto.response.UpdateProductResponse;
import com.zb.fresh_api.api.utils.S3Uploader;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.dto.file.UploadedFile;
import com.zb.fresh_api.domain.entity.address.DeliveryAddress;
import com.zb.fresh_api.domain.entity.address.DeliveryAddressSnapshot;
import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.entity.member.Member;
import com.zb.fresh_api.domain.entity.order.ProductOrder;
import com.zb.fresh_api.domain.entity.point.Point;
import com.zb.fresh_api.domain.entity.point.PointHistory;
import com.zb.fresh_api.domain.entity.product.Product;
import com.zb.fresh_api.domain.entity.product.ProductLike;
import com.zb.fresh_api.domain.entity.product.ProductSnapshot;
import com.zb.fresh_api.domain.enums.category.CategoryType;
import com.zb.fresh_api.domain.enums.point.PointTransactionType;
import com.zb.fresh_api.domain.repository.reader.CategoryReader;
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
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductWriter productWriter;
    private final ProductReader productReader;
    private final CategoryReader categoryReader;

    private final ProductSnapshotWriter productSnapshotWriter;
    private final ProductLikeReader productLikeReader;
    private final MemberReader memberReader;
    private final ProductLikeWriter productLikeWriter;
    private final S3Uploader s3Uploader;
    private final DeliveryAddressReader deliveryAddressReader;
    private final PointReader pointReader;
    private final PointHistoryWriter pointHistoryWriter;
    private final DeliveryAddressSnapshotWriter deliveryAddressSnapshotWriter;
    private final ProductOrderWriter productOrderWriter;

    @Transactional
    public AddProductResponse addProduct(AddProductRequest request, Member member,
        MultipartFile image) {
        if (!member.isSeller()) {
            throw new CustomException(ResponseCode.UNAUTHORIZED_ACCESS_EXCEPTION);
        }
        Category category = categoryReader.findById(request.categoryId()).orElseThrow(
            () -> new CustomException(ResponseCode.CATEGORY_NOT_FOUND)
        );

        final UploadedFile file = s3Uploader.upload(CategoryType.PRODUCT, image);


        Product storedProduct = productWriter.store(Product.create(request, category, member, file.url()));
        return new AddProductResponse(storedProduct.getId(), storedProduct.getName(), storedProduct.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public GetProductDetailResponse getProductDetail(final Long productId) {
        Product product = productReader.findById(productId)
            .orElseThrow(() -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND));

        return GetProductDetailResponse.fromEntity(product);
    }

    @Transactional
    public UpdateProductResponse updateProduct(final Long productId, final UpdateProductRequest request, final MultipartFile imageRequest,
        final Member loginMember) {
        Product product = productReader.findById(productId)
            .orElseThrow(() -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND));

        if(!Objects.equals(loginMember.getId(), product.getMember().getId())){
            throw  new CustomException(ResponseCode.NOT_PRODUCT_OWNER);
        }

        productSnapshotWriter.store(ProductSnapshot.create(product));

        final String newProfileImage = convertImage(imageRequest);
        Category newCategory = findCategory(request.categoryId());
        product.update(request, newProfileImage, newCategory);


        return new UpdateProductResponse(product.getId());
    }

    // TODO 1. 이미지 변환 (S3)
    private String convertImage(MultipartFile imageRequest) {
        if (imageRequest != null && !imageRequest.isEmpty()) {
            // 이미지 변환 로직 (예: S3 업로드)
            return "newProfileImageUrl"; // 실제 변환된 이미지 URL을 반환
        }
        return null;
    }

    private Category findCategory(Long categoryId) {
        if (categoryId != null) {
            return categoryReader.findById(categoryId)
                .orElseThrow(() -> new CustomException(ResponseCode.CATEGORY_NOT_FOUND));
        }
        return null;
    }

//    @Transactional
    public DeleteProductResponse deleteProduct(DeleteProductRequest request, Member member) {
        Product product = productReader.findById(request.productId()).orElseThrow(
            () -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND));

        if(!Objects.equals(member.getId(), product.getMember().getId())){
            throw new CustomException(ResponseCode.UNAUTHORIZED_ACCESS_EXCEPTION);
        }

        Product.delete(product);
        productWriter.store(product);
        return new DeleteProductResponse(product.getId());

    }


    // 추가되는 엔티티
    // PointHistory, ProductSnapshot, DeliveryAddressSnapshot, ProductOrder
    // 업데이트되는 엔티티
    // Point, Product
    @Transactional
    public BuyProductResponse buyProduct(Long productId,  BuyProductRequest request, Long memberId){
        // productId 유효, memberId 유효, deliveryAddressId 유효 검사
        Product product = productReader.getById(productId);
        Member member = memberReader.getById(memberId);
        DeliveryAddress deliveryAddress = deliveryAddressReader.getActiveDeliveryAddressByIdAndMemberId(
            request.deliveryAddressId(), memberId);
        Point memberPoint = pointReader.getByMemberId(memberId);
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.quantity()));

        // 포인트 부족한지 검사, 재고가 부족한지 검사
        checkBalanceEnough(memberPoint,totalPrice);
        checkQuantityEnough(request, product);

        // point_history 추가, point balance 감소
        PointHistory pointHistory = PointHistory.create(memberPoint, PointTransactionType.PAYMENT,
            totalPrice, memberPoint.getBalance(),
            memberPoint.getBalance().subtract(totalPrice), "상품 구매");
        pointHistoryWriter.store(pointHistory);
        memberPoint.pay(totalPrice);

        // product의 quantity 감소
        product.decreaseQuantity(request.quantity());

        // product_snapshot 생성, deliveryAddress_snapshot 생성
        ProductSnapshot productSnapshot = ProductSnapshot.create(product);
        productSnapshotWriter.store(productSnapshot);
        DeliveryAddressSnapshot deliveryAddressSnapshot = DeliveryAddressSnapshot.of(deliveryAddress);
        deliveryAddressSnapshotWriter.store(deliveryAddressSnapshot);

        // product_order 생성
        ProductOrder productOrder = ProductOrder.create(productSnapshot, deliveryAddressSnapshot);
        productOrderWriter.store(productOrder);


        return new BuyProductResponse(productOrder.getId(), productOrder.getProductSnapshot().getName(),
            productOrder.getDeliveryAddressSnapshot().getRecipientName(), totalPrice);
    }

    private static void checkQuantityEnough(BuyProductRequest request, Product product) {
        if(product.getQuantity() < request.quantity()){
            throw new CustomException(ResponseCode.NOT_ENOUGH_QUANTITY);
        }
    }

    private void checkBalanceEnough(Point memberPoint, BigDecimal totalPrice) {
        if(memberPoint.getBalance().compareTo(totalPrice) < 0){
            throw new CustomException(ResponseCode.POINT_NOT_ENOUGH);
        }
    }


    public GetAllProductByConditionsResponse findProducts(GetAllProductByConditionsRequest request) {
        Page<Product> products = productReader.findAll(request);

        return GetAllProductByConditionsResponse.fromEntities(products);
    }

    public FindAllProductLikeResponse findAllProductLike(Long memberId){
        List<Long> productLikes = productLikeReader.findProductIdByMemberId(memberId);

        List<Product> productList = new ArrayList<>();
        for(Long productId : productLikes){
            Product product = productReader.findById(productId).orElseThrow(
                () -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND)
            );
            productList.add(product);
        }

        return FindAllProductLikeResponse.fromEntities(productList);
    }

    public LikeProductResponse like(Long productId,Long memberId) {
        Product product = productReader.findById(productId).orElseThrow(
            () -> new CustomException(ResponseCode.PRODUCT_NOT_FOUND)
        );
        Member member = memberReader.getById(memberId);

        if(productLikeReader.isExist(productId,memberId)){
            throw  new CustomException(ResponseCode.PRODUCT_ALREADY_LIKED);
        }

        ProductLike productLike = productLikeWriter.store(ProductLike.create(member, product));

        return new LikeProductResponse(productLike);
    }

    public void unLike(Long productId,Long memberId) {
        productReader.findById(productId).orElseThrow(
            ()-> new CustomException(ResponseCode.PRODUCT_NOT_FOUND)
        );
        memberReader.getById(memberId);

        ProductLike productLike = productLikeReader.findByProductIdAndMemberId(productId, memberId)
            .orElseThrow(() -> new CustomException(ResponseCode.PRODUCT_LIKE_NOT_FOUND));

        productLikeWriter.delete(productLike);
    }

}
