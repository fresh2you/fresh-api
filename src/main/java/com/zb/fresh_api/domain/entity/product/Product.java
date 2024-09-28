package com.zb.fresh_api.domain.entity.product;

import com.zb.fresh_api.api.dto.request.AddProductRequest;
import com.zb.fresh_api.api.dto.request.UpdateProductRequest;
import com.zb.fresh_api.domain.entity.base.BaseTimeEntity;
import com.zb.fresh_api.domain.entity.category.Category;
import com.zb.fresh_api.domain.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(
    name = "product"
)
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT UNSIGNED comment '고유 번호'")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '회원 고유 번호'")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, columnDefinition = "BIGINT UNSIGNED comment '카테고리 고유 번호'")
    private Category category;

    @JoinColumn(name = "name", nullable = false, columnDefinition = "varchar(20)  comment '상품명'")
    private String name;

    @JoinColumn(name = "description", nullable = false, columnDefinition = "text  comment '상품설명'")
    private String description;

    @Column(name = "quantity", nullable = false, columnDefinition = "INT UNSIGNED comment '재고'")
    private int quantity;

    @Column(name = "price", nullable = false, precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) comment '가격'")
    private BigDecimal price;
    
    @Column(name = "product_image", columnDefinition = "varchar(255) comment '상품 이미지'")
    private String productImage;
    
    @Column(name = "deleted_at", columnDefinition = "datetime comment '삭제일'")
    private LocalDateTime deletedAt;

    public static Product create(AddProductRequest request,Category category, Member member, String productImage){
        return Product.builder()
            .member(member)
            .category(category)
            .name(request.name())
            .description(request.description())
            .quantity(request.quantity())
            .price(request.price())
            .productImage(productImage)
            .build();
    }

    public void update(UpdateProductRequest request, String productImageUrl, Category category){
        if(category != null) this.category = category;
        if(request.name() != null) this.name = request.name();
        if(request.description() != null) this.description = request.description();
        if(request.quantity() != null) this.quantity = request.quantity();
        if(request.price() != null) this.price = request.price();
        if(productImageUrl != null) this.productImage = productImageUrl;
    }

    public static void delete(Product product){
        product.deletedAt = LocalDateTime.now();
    }

    public void decreaseQuantity(int quantity){
        this.quantity = this.quantity - quantity;
    }
}
