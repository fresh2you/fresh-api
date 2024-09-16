package com.zb.fresh_api.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.fresh_api.common.exception.ResponseCode;
import com.zb.fresh_api.common.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ObjectMapper objectMapper = new ObjectMapper();
        String errorResponse = objectMapper.writeValueAsString(ApiResponse.error(ResponseCode.FORBIDDEN, accessDeniedException));
        response.getWriter().write(errorResponse);
    }
}
