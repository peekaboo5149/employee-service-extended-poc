package com.deloitte.employees.presentation.handlers;

import com.deloitte.employees.common.models.ErrorDetail;
import com.deloitte.employees.helper.WebConstants;
import com.deloitte.employees.presentation.exception.ErrorCode;
import com.deloitte.employees.presentation.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Component
class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ErrorResponse error = ErrorResponse.builder()
                .message("Authentication failed")
                .code(401)
                .errorCode(ErrorCode.UNAUTHORIZED)
                .errorDetails(List.of(
                        ErrorDetail.builder()
                                .code("AUTHENTICATION_FAILED")
                                .field("token")
                                .message(authException.getMessage())
                                .build()
                ))
                .build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(WebConstants.JSON_CONTENT_TYPE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}
