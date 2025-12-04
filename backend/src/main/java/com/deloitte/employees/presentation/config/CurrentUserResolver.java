package com.deloitte.employees.presentation.config;

import com.deloitte.employees.presentation.dto.response.EmployeeBasicDetail;
import com.deloitte.employees.presentation.expression.CurrentUser;
import com.deloitte.employees.presentation.utils.AuthenticationHelper;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
class CurrentUserResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class)
                && parameter.getParameterType().equals(EmployeeBasicDetail.class);
    }

    @Override
    public @Nullable Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        EmployeeBasicDetail principal = AuthenticationHelper.getPrincipal();

        if (principal == null || !principal.isVerified()) {
            throw new AccessDeniedException("User is not verified");
        }

        return principal;
    }
}
