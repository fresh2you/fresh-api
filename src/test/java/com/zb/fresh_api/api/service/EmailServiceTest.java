package com.zb.fresh_api.api.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zb.fresh_api.api.enums.CertificationType;
import com.zb.fresh_api.api.utils.CertificationCodeUtil;
import com.zb.fresh_api.api.utils.EmailUtil;
import com.zb.fresh_api.api.utils.RedisUtil;
import com.zb.fresh_api.common.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private EmailUtil emailUtil;

    @Test
    @DisplayName("메일 전송 실패_횟수 초과")
    public void sendMail_fail_exceededAttemptLimit() {
        String email = "test@example.com";
        when(redisUtil.hasExceededAttemptLimit(email, CertificationType.EMAIL)).thenReturn(true);

        assertThrows(CustomException.class, () -> emailService.sendMail(email));
        verify(redisUtil, times(1)).hasExceededAttemptLimit(email, CertificationType.EMAIL);
    }

    @Test
    @DisplayName("메일 전송 성공")
    public void sendMail_success() {
        String email = "test@example.com";
        String certificationCode = "132446";

        when(redisUtil.hasExceededAttemptLimit(email, CertificationType.EMAIL)).thenReturn(false);
        doNothing().when(emailUtil).sendCertificationCode(email, certificationCode);
        doNothing().when(redisUtil).saveCertificationCode(email, certificationCode, CertificationType.EMAIL);
        doNothing().when(redisUtil).recordAttempt(email, CertificationType.EMAIL);

        try (MockedStatic<CertificationCodeUtil> certificationCodeUtilMockedStatic = mockStatic(CertificationCodeUtil.class)) {
            certificationCodeUtilMockedStatic.when(CertificationCodeUtil::generateRandomString).thenReturn(certificationCode);

            emailService.sendMail(email);

            verify(redisUtil, times(1)).hasExceededAttemptLimit(email, CertificationType.EMAIL);
            verify(emailUtil, times(1)).sendCertificationCode(email, certificationCode);
            verify(redisUtil, times(1)).saveCertificationCode(email, certificationCode, CertificationType.EMAIL);
            verify(redisUtil, times(1)).recordAttempt(email, CertificationType.EMAIL);
        }
    }
    @Test
    @DisplayName("이메일 인증 실패_인증이 존재하지 않거나 인증 유효시간을 초과")
    public void certificateCode_fail_notFound() {
        String email = "test@example.com";
        String certificationCode = "123456";
        when(redisUtil.findSmsCertification(email, CertificationType.EMAIL)).thenReturn(null);
        assertThrows(CustomException.class, () -> emailService.certificateCode(email, certificationCode));
        verify(redisUtil, times(1)).findSmsCertification(email, CertificationType.EMAIL);
    }

    @Test
    @DisplayName("인증 코드가 일치하지 않습니다")
    public void certificateCode_fail_notCorrect() {
        String email = "test@example.com";
        String certificationCode = "123456";
        when(redisUtil.findSmsCertification(email, CertificationType.EMAIL)).thenReturn("654321");
        assertThrows(CustomException.class, () -> emailService.certificateCode(email, certificationCode));
        verify(redisUtil, times(1)).findSmsCertification(email, CertificationType.EMAIL);
    }

    @Test
    @DisplayName("인증 코드 인증 성공")
    public void certificateCode_success() {
        String email = "test@example.com";
        String certificationCode = "123456";
        when(redisUtil.findSmsCertification(email, CertificationType.EMAIL)).thenReturn(certificationCode);
        doNothing().when(redisUtil).removeSmsCertification(email, CertificationType.EMAIL);

        emailService.certificateCode(email, certificationCode);

        verify(redisUtil, times(1)).findSmsCertification(email, CertificationType.EMAIL);
        verify(redisUtil, times(1)).removeSmsCertification(email, CertificationType.EMAIL);
    }
}
