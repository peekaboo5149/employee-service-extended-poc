package com.deloitte.employees.presentation.utils;

import com.deloitte.employees.presentation.dto.response.EmployeeBasicDetail;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class AuthenticationHelper {

    public static EmployeeBasicDetail getPrincipal() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx.getAuthentication() == null) return null;
        return (EmployeeBasicDetail) ctx
                .getAuthentication()
                .getPrincipal();
    }
}
