package com.deloitte.employees.presentation.services.impl;

import com.deloitte.employees.domain.auth.entities.Employee;
import com.deloitte.employees.helper.WebConstants;
import com.deloitte.employees.presentation.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
class JwtServiceImpl implements JwtService {

    private final JwtConfigProperties jwtConfigProperties;

    /**
     * Extracts the username (subject) from the JWT.
     *
     * @param jwt The JWT token.
     * @return The username (email) stored in the subject claim.
     */
    @Override
    public String extractUsername(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    /**
     * Checks if a token is valid for a given UserDetails.
     *
     * @param jwt         The JWT token.
     * @param userDetails The UserDetails object for comparison.
     * @return True if the token is valid (username matches and is not expired).
     */
    @Override
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwt);
    }

    // --- Private Helper Methods ---

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfigProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // --- Token Generation (Required for EmployeeAuthenticationServiceImpl) ---

    /**
     * Generates a JWT token for the provided UserDetails.
     * This method is needed in EmployeeAuthenticationServiceImpl
     *
     * @param employee The UserDetails to base the token claims on.
     * @return The generated JWT string.
     */
    @Override
    public String generateToken(Employee employee) {
        return generateToken(new HashMap<>(), employee);
    }

    /**
     * Generates a JWT token with custom claims.
     *
     * @param extraClaims Additional claims (e.g., roles).
     * @param employee    The UserDetails to base the token claims on.
     * @return The generated JWT string.
     */
    public String generateToken(Map<String, Object> extraClaims, Employee employee) {
        extraClaims.put(WebConstants.ID, employee.getId().getValue());
        extraClaims.put(WebConstants.STATUS, employee.getStatus().name());
        extraClaims.put(WebConstants.ROLE, employee.getRole().name());
        extraClaims.put(WebConstants.IS_VERIFIED, employee.getIsVerified() != null && employee.getIsVerified());
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(employee.getEmail().getValue())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}