package com.deloitte.employees.presentation.services;

import com.deloitte.employees.presentation.dto.AuthenticationRequest;
import com.deloitte.employees.presentation.dto.RegistrationRequest;
import com.deloitte.employees.presentation.dto.request.TokenResponse;

public interface EmployeeAuthenticationService {

    TokenResponse authenticate(AuthenticationRequest authenticationRequest);
    TokenResponse registration(RegistrationRequest registrationRequest);

}
