package com.deloitte.employees.presentation.controller;

import com.deloitte.employees.presentation.dto.request.AuthenticationRequest;
import com.deloitte.employees.presentation.dto.request.RegistrationRequest;
import com.deloitte.employees.presentation.services.EmployeeAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static com.deloitte.employees.presentation.controller.Constants.API_AUTH_V1;

@RestController
@RequestMapping(API_AUTH_V1)
@RequiredArgsConstructor
class EmployeeAuthenticationController {

    private final EmployeeAuthenticationService employeeAuthenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest req) {
        return ResponseEntity
                .created(URI.create(API_AUTH_V1 + "/login"))
                .body(employeeAuthenticationService.authenticate(req));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest req) {
        return ResponseEntity
                .created(URI.create(API_AUTH_V1 + "/register"))
                .body(employeeAuthenticationService.registration(req));
    }

}
