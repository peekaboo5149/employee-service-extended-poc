package com.deloitte.employees.presentation.services;

import com.deloitte.employees.presentation.dto.request.AuthenticationRequest;
import com.deloitte.employees.presentation.dto.request.RegistrationRequest;
import com.deloitte.employees.presentation.dto.response.TokenResponse;

public interface EmployeeAuthenticationService {

    TokenResponse authenticate(AuthenticationRequest authenticationRequest);
    TokenResponse registration(RegistrationRequest registrationRequest);

}
