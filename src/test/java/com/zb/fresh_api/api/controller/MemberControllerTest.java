package com.zb.fresh_api.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.fresh_api.api.dto.SignUpRequest;
import com.zb.fresh_api.api.dto.TermsAgreementDto;
import com.zb.fresh_api.api.provider.TokenProvider;
import com.zb.fresh_api.api.service.MemberService;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.domain.enums.member.Provider;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MemberController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class MemberControllerTest {
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @Test
    @DisplayName("닉네임 중복 검사 성공")
    void checkNicknameAvailability_success() throws Exception {
        String nickname = "validNickname";

        mockMvc.perform(get("/v1/api/members/check-nickname")
                .param("nickname", nickname))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));

        verify(memberService, times(1)).nickNameValidate(nickname);
    }

    @Test
    @DisplayName("닉네임 중복 검사 실패(닉네임 유효하지않음)")
    void checkEmailAvailability_fail_nicknameInvalid() throws Exception {
        String nickname = "";

        doThrow(new CustomException(ResponseCode.PARAM_EMAIL_NOT_VALID))
            .when(memberService).nickNameValidate(nickname);

        mockMvc.perform(get("/v1/api/members/check-nickname")
                .param("nickname", nickname))
            .andExpect(jsonPath("$.message").value(ResponseCode.PARAM_EMAIL_NOT_VALID.getMessage()));


        verify(memberService, times(1)).nickNameValidate(nickname);
    }

    @Test
    @DisplayName("이메일 중복 검사 성공")
    void checkEmailAvailability_success() throws Exception {
        String email = "test@example.com";

        mockMvc.perform(get("/v1/api/members/check-email")
                .param("email", email))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));

        verify(memberService, times(1)).emailValidate(email, Provider.EMAIL);
    }

    @Test
    @DisplayName("이메일 중복 검사 실패")
    void checkEmailAvailability_fail_emailInvalid() throws Exception {
        //given
        doThrow(new CustomException(ResponseCode.PARAM_EMAIL_NOT_VALID))
            .when(memberService).emailValidate(anyString(), any(Provider.class));


        mockMvc.perform(get("/v1/api/members/check-email")
                .param("email", ""))
            .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() throws Exception {
        TermsAgreementDto termsAgreementDto = new TermsAgreementDto(1L, true);
        SignUpRequest request = new SignUpRequest("test@example.com", "password",
            "password", "nickname", List.of(termsAgreementDto), Provider.EMAIL,
            "1");

        mockMvc.perform(post("/v1/api/members/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(ResponseCode.SUCCESS.getCode()));

        verify(memberService, times(1)).signUp(request.email(), request.password(),
            request.nickname(), request.termsAgreements(), request.provider(),request.providerId());
    }

}
