package com.tanle.e_commerce.service;

import io.jsonwebtoken.Claims;
import com.tanle.e_commerce.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;


import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public interface JwtService {
    String extractUserName(String token);
    <T> T extractClaims(String token, Function<Claims,T> claimsResolver);
    String gernarateKey(UserDetails userDetails);
    String gernarateKey(Map<String, Object> claims, UserDetails userDetails);
    boolean isValidToken(UserDetails userDetails, String token);
     boolean isTokenExpired(String token);

}
