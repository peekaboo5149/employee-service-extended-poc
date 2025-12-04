package com.deloitte.employees.presentation.controller;

import com.deloitte.employees.presentation.dto.response.EmployeeBasicDetail;
import com.deloitte.employees.presentation.expression.CurrentUser;
import com.deloitte.employees.presentation.expression.VerifiedAdmin;
import com.deloitte.employees.presentation.expression.VerifiedUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestAuthorizationController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/only")
    public String adminEndpoint() {
        return "You are admin!";
    }


    @VerifiedAdmin
    @GetMapping("/admin/panel")
    public String adminPanel() {
        return "Welcome Admin!";
    }

    @VerifiedUser
    @GetMapping("/profile")
    public String profile(@CurrentUser EmployeeBasicDetail user) {
        return user.toString();
    }


}
