package com.zb.fresh_api.api.utils;

import com.zb.fresh_api.common.constants.EmailConstants;
import com.zb.fresh_api.common.exception.CustomException;
import com.zb.fresh_api.common.exception.ResponseCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "test@gmail.com";

    public MimeMessage CreateMail(String mail, String certificationCode) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("Fresh2You 이메일 인증");

            String body = EmailConstants.createCertificationCodeHtml(certificationCode);
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new CustomException(ResponseCode.GOOGLE_SMTP_ERROR);
        }
        return message;
    }

    // 인증 전송 메서드
    public void sendCertificationCode(String mail, String certificatinoCode) {
        MimeMessage message = CreateMail(mail, certificatinoCode);
        javaMailSender.send(message);
    }
}
