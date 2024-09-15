package com.zb.fresh_api.api.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zb.fresh_api.api.dto.TermsDto;
import com.zb.fresh_api.api.provider.TokenProvider;
import com.zb.fresh_api.api.service.TermsService;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TermsController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class TermsControllerTest {

    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private TermsService termsService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityFilterChain securityFilterChain;
    @Test
    @DisplayName("약관 조회 성공")
    void getAllTerms_success() throws Exception {
        mockMvc.perform(get("/v1/api/terms"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));
        verify(termsService, times(1)).getTerms();

    }

    @Test
    @DisplayName("약관 상세 조회 성공")
    void getTermsById_success() throws Exception {
        TermsDto termsDto = TermsDto.builder().build();
        given(termsService.getTerm(1L)).willReturn(termsDto);

        mockMvc.perform(get("/v1/api/terms/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));
        verify(termsService, times(1)).getTerm(1L);
    }

    @Test
    @DisplayName("약관 상세 조회 실패(유효하지 않은 약관 id)")
    void getTermsById_fail_invalidTermsId() throws Exception {
        // given
        given(termsService.getTerm(anyLong())).willThrow(
            new CustomException(ResponseCode.TERMS_NOT_FOUND));

        mockMvc.perform(get("/v1/api/terms/{id}", 5))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(ResponseCode.TERMS_NOT_FOUND.getCode()));
        verify(termsService, times(1)).getTerm(5L);
    }
}
