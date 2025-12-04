package com.deloitte.employees.presentation.services.impl;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("application.jwt")
class JwtConfigProperties {
    private String secret;
}
