package com.deloitte.employees.presentation.services;

import com.deloitte.employees.domain.auth.entities.Employee;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JwtService {
    String extractUsername(String jwt);
    boolean isTokenValid(String jwt, UserDetails userDetails);
    String generateToken(Employee employee);
    String generateToken(Map<String, Object> extraClaims, Employee employee);
}
