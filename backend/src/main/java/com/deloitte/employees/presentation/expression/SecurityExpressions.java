package com.deloitte.employees.presentation.expression;

import com.deloitte.employees.common.enums.Role;
import com.deloitte.employees.presentation.dto.response.EmployeeBasicDetail;
import com.deloitte.employees.presentation.utils.AuthenticationHelper;
import org.springframework.stereotype.Component;

@Component("sec")
public class SecurityExpressions {

    public boolean isVerified() {
        EmployeeBasicDetail principal = AuthenticationHelper.getPrincipal();
        return principal != null && principal.isVerified();
    }

    public boolean isAdmin() {
        EmployeeBasicDetail principal = AuthenticationHelper.getPrincipal();
        return principal != null && Role.ADMIN.equals(principal.getRole());
    }

    public boolean isUser() {
        EmployeeBasicDetail principal = AuthenticationHelper.getPrincipal();
        return principal != null && Role.USER.equals(principal.getRole());
    }

    public boolean isVerifiedAdmin() {
        return isVerified() && isAdmin();
    }

    public boolean isVerifiedUser() {
        return isVerified() && isUser();
    }

}
