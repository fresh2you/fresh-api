package com.zb.fresh_api.common.constants;

public class EmailConstants {
    public static String createVerificationCodeHtml(String certificationCode){
        return "<!DOCTYPE html>" +
            "<html lang=\"ko\">" +
            "<head>" +
            "<meta charset=\"UTF-8\">" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
            "<title>Fresh2You 이메일 인증</title>" +
            "</head>" +
            "<body style=\"font-family: 'Helvetica Neue', Arial, sans-serif; line-height: 1.6; background-color: #f4f4f4; margin: 0; padding: 0;\">" +
            "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);\">" +
            "<tr>" +
            "<td style=\"padding: 40px 30px; text-align: center; background-color: #4CAF50; border-radius: 8px 8px 0 0;\">" +
            "<h1 style=\"color: #ffffff; font-size: 28px; margin: 0;\">Fresh2You 이메일 인증</h1>" +
            "</td>" +
            "</tr>" +
            "<tr>" +
            "<td style=\"padding: 40px 30px;\">" +
            "<p style=\"font-size: 16px; color: #333333;\">안녕하세요,</p>" +
            "<p style=\"font-size: 16px; color: #333333;\">Fresh2You 서비스 이용을 위한 이메일 인증 번호입니다:</p>" +
            "<div style=\"background-color: #f8f8f8; border: 1px solid #e0e0e0; border-radius: 4px; padding: 20px; margin: 30px 0; text-align: center;\">" +
            "<h2 style=\"color: #4CAF50; font-size: 32px; margin: 0;\">" + certificationCode + "</h2>" +
            "</div>" +
            "<p style=\"font-size: 16px; color: #333333;\">이 인증 번호를 입력하여 이메일 인증을 완료해 주세요.</p>" +
            "<p style=\"font-size: 16px; color: #333333;\">문의사항이 있으시면 언제든 고객센터로 연락 주시기 바랍니다.</p>" +
            "</td>" +
            "</tr>" +
            "<tr>" +
            "<td style=\"padding: 30px; background-color: #f4f4f4; text-align: center; border-radius: 0 0 8px 8px;\">" +
            "<p style=\"font-size: 14px; color: #888888; margin: 0;\">감사합니다.</p>" +
            "<p style=\"font-size: 14px; color: #888888; margin: 5px 0 0;\">Fresh2You 팀 드림</p>" +
            "</td>" +
            "</tr>" +
            "</table>" +
            "</body>" +
            "</html>";
    }
}
