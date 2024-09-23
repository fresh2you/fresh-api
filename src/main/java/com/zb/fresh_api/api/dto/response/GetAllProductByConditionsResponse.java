package com.zb.fresh_api.api.dto.response;

import com.zb.fresh_api.domain.entity.product.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

@Schema(description = "상품 목록 조회 시 사용하는 응답")
public record GetAllProductByConditionsResponse (
    @Schema(description = "상품 목록")
    List<ProductByConfitionsDto> productList,
    
    @Schema(description = "현재 페이지 번호")
    int pageNumber,
    
    @Schema(description =  "현재 페이지 크기")
    int pageSize,

    @Schema(description = "전체 페이지 번호")
    int totalPageNumber
){
    public static GetAllProductByConditionsResponse fromEntities(Page<Product> products){
        return new GetAllProductByConditionsResponse(
            products.stream().map(ProductByConfitionsDto::fromEntity).toList(),products.getPageable().getPageNumber(),
            products.getContent().size(),products.getTotalPages()
        );
    }
}
