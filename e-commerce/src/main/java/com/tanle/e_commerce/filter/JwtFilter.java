package com.tanle.e_commerce.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanle.e_commerce.Repository.Jpa.TokenRepository;
import com.tanle.e_commerce.entities.Token;
import com.tanle.e_commerce.exception.ExceptionResponse;
import com.tanle.e_commerce.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenRepository tokenRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String jwtToken;
            String userName;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwtToken = authHeader.substring(7);
            userName = jwtService.extractUserName(jwtToken);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
                boolean isTokenValid = tokenRepository.findTokenByToken(jwtToken)
                        .map(t -> !t.isRevoked() && !t.isExpired())
                        .orElse(false);
                if (jwtService.isValidToken(userDetails, jwtToken) && isTokenValid) {
//                    List<String> roles = jwtService.extractRoles(jwtToken);
//                    var currentAuthorities = userDetails.getAuthorities()
//                            .stream()
//                            .filter(a -> roles.contains(a.getAuthority()))
//                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                    .type("/exception/"+ e.getClass().getSimpleName())
                    .timeStamp(System.currentTimeMillis())
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .title("JWT token expired")
                    .detail("JWT token expired. Please refresh the token.")
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(),exceptionResponse);
        }
    }
}
