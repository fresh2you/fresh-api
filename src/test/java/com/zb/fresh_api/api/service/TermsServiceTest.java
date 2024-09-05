package com.zb.fresh_api.api.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.zb.fresh_api.api.dto.TermsDto;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.entity.terms.Terms;
import com.zb.fresh_api.domain.repository.jpa.TermsRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TermsServiceTest {
    @Mock
    private TermsRepository termsRepository;

    @InjectMocks
    private TermsService termsService;


    private Terms terms;
    private TermsDto termsDto;

    @BeforeEach
    void setUp() {
        terms = Terms.builder()
            .id(1L)
            .title("약관 타이틀")
            .isRequired(true)
            .content("약관 내용")
            .build();
        termsDto = TermsDto.fromEntity(terms);
    }
    @Test
    @DisplayName("약관 목록 조회 성공")
    void getTerms_success(){
        // given
        when(termsRepository.findAll()).thenReturn(Collections.singletonList(terms));
        // when
        List<TermsDto> result = termsService.getTerms();
        // then
        assertNotNull(result);
        assertEquals(termsDto, result.get(0));
    }

    @Test
    @DisplayName("약관 상세 조회 성공")
    void getTerm_success(){
        // given
        when(termsRepository.findById(anyLong())).thenReturn(Optional.of(terms));
        // when
        TermsDto result = termsService.getTerm(1L);
        // then
        assertNotNull(result);
        assertEquals(termsDto, result);
    }

    @Test
    @DisplayName("약관 상세 조회 실패(약관이 없음)")
    void getTerm_fail_terms_not_found(){
        // given
        when(termsRepository.findById(anyLong())).thenReturn(Optional.empty());
        // then
        CustomException customException = assertThrows(CustomException.class,
            () -> termsService.getTerm(1L));
        assertEquals(ResponseCode.TERMS_NOT_FOUND, customException.getResponseCode());
    }
}
