package com.deloitte.employees.presentation.services.impl;

import com.deloitte.employees.common.exceptions.OperationFailure;
import com.deloitte.employees.domain.auth.entities.Employee;
import com.deloitte.employees.domain.auth.repositories.EmployeeRepositoryFacade;
import com.deloitte.employees.domain.auth.valueobjects.Email;
import com.deloitte.employees.domain.auth.valueobjects.Password;
import com.deloitte.employees.domain.auth.valueobjects.UniqueId;
import com.deloitte.employees.presentation.dto.AuthenticationRequest;
import com.deloitte.employees.presentation.dto.RegistrationRequest;
import com.deloitte.employees.presentation.dto.request.TokenResponse;
import com.deloitte.employees.presentation.mapper.AppExceptionMapper;
import com.deloitte.employees.presentation.services.EmployeeAuthenticationService;
import com.deloitte.employees.presentation.services.JwtService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class EmployeeAuthenticationServiceImpl implements EmployeeAuthenticationService {

    private final EmployeeRepositoryFacade employeeRepositoryFacade;
    private final AppExceptionMapper appExceptionMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public TokenResponse authenticate(AuthenticationRequest authenticationRequest) {
        Email email = Email.create(authenticationRequest.getEmail()).getOrElseThrow(appExceptionMapper::map);
        Option<Employee> employee = employeeRepositoryFacade.findByEmail(email).getOrElseThrow(appExceptionMapper::map);
        if (!employee.isDefined()) {
            throw new UsernameNotFoundException("Email/Password combination not found");
        }
        Password password = Password.create(authenticationRequest.getPassword())
                .getOrElseThrow(appExceptionMapper::map);
        if (!passwordEncoder.matches(password.getValue(), employee.get().getPassword().getValue())) {
            throw new BadCredentialsException("Email/Password combination not found");
        }
        String jwtToken = jwtService.generateToken(employee.get());
        return TokenResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public TokenResponse registration(RegistrationRequest registrationRequest) {
        Email email = Email.create(registrationRequest.getEmail()).getOrElseThrow(appExceptionMapper::map);
        Password password = Password.create(passwordEncoder.encode(registrationRequest.getPassword()))
                .getOrElseThrow(appExceptionMapper::map);
        Employee employee = Employee.builder()
                .id(UniqueId.generate())
                .email(email)
                .password(password)
                .build();
        Either<OperationFailure, Employee> result
                = employeeRepositoryFacade.save(employee);
        if (result.isLeft()) throw appExceptionMapper.map(result.getLeft());
        Employee savedEmployee = result.get();
        String jwtToken = jwtService.generateToken(savedEmployee);
        return TokenResponse.builder()
                .token(jwtToken)
                .build();
    }
}
