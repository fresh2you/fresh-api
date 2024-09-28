package com.zb.fresh_api.api.service;

import com.zb.fresh_api.common.base.ServiceTest;
import com.zb.fresh_api.domain.repository.reader.OrderReader;
import com.zb.fresh_api.domain.repository.reader.ProductReader;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("상품 추천 비즈니스 로직 테스트")
class RecommendServiceTest extends ServiceTest {

    @Mock
    private OrderReader orderReader;

    @Mock
    private ProductReader productReader;

    @InjectMocks
    private RecommendService recommendService;

//    @Test
//    void 상품_랜덤_추천() {
//        // given
//        LoadProductRecommendListRequest request = getConstructorMonkey().giveMeBuilder(LoadProductRecommendListRequest.class)
//                .set("size", Arbitraries.integers().between(1, 10).sample())
//                .sample();
//
//        int size = request.size();
//
//        List<RecommendProductSummary> productSummaries = Stream.generate(() -> getConstructorMonkey().giveMeBuilder(RecommendProductSummary.class).sample())
//                .limit(size)
//                .toList();
//
//        // when
//        doReturn(productSummaries).when(productReader).getAllRandomProduct(size);
//
//        // then
//        LoadProductRecommendListResponse response = recommendService.loadRecommendedRandomProductList(request);
//
//        assertNotNull(response);
//        assertEquals(response.count(), productSummaries.size());
//
//        verify(productReader, times(1)).getAllRandomProduct(size);
//    }

}
