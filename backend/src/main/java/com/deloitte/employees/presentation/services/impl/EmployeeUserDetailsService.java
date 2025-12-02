package com.deloitte.employees.presentation.services.impl;

import com.deloitte.employees.domain.auth.entities.Employee;
import com.deloitte.employees.domain.auth.repositories.EmployeeRepositoryFacade;
import com.deloitte.employees.domain.auth.valueobjects.Email;
import com.deloitte.employees.presentation.mapper.AppExceptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EmployeeUserDetailsService implements UserDetailsService {

    private final EmployeeRepositoryFacade employeeRepositoryFacade;
    private final AppExceptionMapper appExceptionMapper;

    @Override
    public UserDetails loadUserByUsername(String emailStr) throws UsernameNotFoundException {
        Email email = Email.create(emailStr)
                .getOrElseThrow(appExceptionMapper::map);
        Employee employee = employeeRepositoryFacade.findByEmail(email)
                .getOrElseThrow(appExceptionMapper::map)
                .getOrElseThrow(() -> new UsernameNotFoundException("Employee with email " + emailStr + " not found"));
        return new User(
                employee.getEmail().getValue(),
                employee.getPassword().getValue(),
                new java.util.ArrayList<>()
        );

    }
}
