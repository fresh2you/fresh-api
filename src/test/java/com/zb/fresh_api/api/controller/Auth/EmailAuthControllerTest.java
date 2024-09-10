package com.zb.fresh_api.api.controller.Auth;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zb.fresh_api.api.provider.TokenProvider;
import com.zb.fresh_api.api.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = EmailAuthController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class EmailAuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("이메일 전송 API 성공")
    @Test
    public void testSendVerificationCode_success() throws Exception {
        String email = "test@example.com";

        doNothing().when(emailService).sendMail(email);

        mockMvc.perform(get("/v1/api/auth/email")
                .param("email", email)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(emailService).sendMail(email);
    }

    @DisplayName("이메일 인증 코드 전송 성공")
    @Test
    public void testVerificationEmailCode_success() throws Exception {
        String email = "test@example.com";
        String veritificationCode = "123456";

        doNothing().when(emailService).verifyCode(email, veritificationCode);

        mockMvc.perform(get("/v1/api/auth/email/verify")
                .param("email", email)
                .param("verificationCode", veritificationCode)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(emailService).verifyCode(email, veritificationCode);
    }
}
