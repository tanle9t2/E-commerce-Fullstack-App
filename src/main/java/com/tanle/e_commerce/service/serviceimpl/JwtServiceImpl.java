package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.tanle.e_commerce.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    private static final String SECRET_KEY = "VBLoLSe1wHc3a4SelQyKN2uExewLWNeTF1XY2EqrFQjuFGtgxtYBXKCPfwqGNrMP";
    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }
    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public String gernarateKey(UserDetails userDetails) {
        return gernarateKey(new HashMap<>(),userDetails);
    }
    public String gernarateKey(Map<String, Object> extralClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extralClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public boolean isValidToken(UserDetails userDetails, String token) {
        String userName = extractUserName(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    public boolean isTokenExpired(String token) {
        return extractExpiredClaims(token).before(new Date());
    }
    public Date extractExpiredClaims(String token) {
        return extractClaims(token,Claims::getExpiration);
    }
    private Key getSignKey() {
        byte [] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
