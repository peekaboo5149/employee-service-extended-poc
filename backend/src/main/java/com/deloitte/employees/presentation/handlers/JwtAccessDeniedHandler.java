package com.deloitte.employees.presentation.handlers;

import com.deloitte.employees.common.models.ErrorDetail;
import com.deloitte.employees.helper.WebConstants;
import com.deloitte.employees.presentation.exception.ErrorCode;
import com.deloitte.employees.presentation.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Component
class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponse error = ErrorResponse.builder()
                .message("Access denied")
                .code(403)
                .errorCode(ErrorCode.FORBIDDEN)
                .errorDetails(List.of(
                        ErrorDetail.builder()
                                .code("ACCESS_DENIED")
                                .field("token")
                                .message(accessDeniedException.getMessage())
                                .build()
                ))
                .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(WebConstants.JSON_CONTENT_TYPE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }
}
