package com.tanle.e_commerce.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanle.e_commerce.Repository.Jpa.TokenRepository;
import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.entities.Token;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.respone.AuthenticationRespone;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.service.JwtService;
import com.tanle.e_commerce.service.TokenSerice;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenSerice {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public MessageResponse registerToken(String username) {
        MyUser myUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", myUser.getUsername());
        claims.put("roles", myUser.getRoles().stream()
                .map(r -> r.getRole().getRoleName())
                .collect(Collectors.toList()));

        String accessToken = jwtService.genarateToken(claims, myUser);
        String refreshToken = jwtService.genarateRefreshToken(claims, myUser);
        saveAccessToken(myUser, accessToken);
        saveRefreshToken(myUser, refreshToken);
        AuthenticationRespone.UserInfor userInfor = AuthenticationRespone.UserInfor.builder()
                .id(myUser.getId())
                .username(username)
                .email(myUser.getUsername())
                .avatar(myUser.getAvtUrl())
                .fullName(myUser.getFullName())
                .build();
        return MessageResponse.builder()
                .data(
                        AuthenticationRespone.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .userInfor(userInfor)
                                .build()
                )
                .message("Register token successfully")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public String authenticate(Map<String, String> request) {
        return null;
    }

    @Override
    public Token findToken(String jwtToken) {
        return tokenRepository.findTokenByToken(jwtToken).get();
    }

    @Override
    @Transactional
    public MessageResponse revokeToken(String username) {
        MyUser myUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user: " + username));
        List<Token> revokeToken = myUser.getTokens().stream()
                .filter(t -> !t.isRevoked() && !t.isRefresh())
                .collect(Collectors.toList());
        revokeToken.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        tokenRepository.saveAll(revokeToken);
        return MessageResponse.builder()
                .data(revokeToken)
                .message("Revoke token successfully")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    @Transactional
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = null;
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return;
            }
            String userName;
            refreshToken = authHeader.substring(7);
            userName = jwtService.extractUserName(refreshToken);
            if (userName != null) {
                MyUser myUserDetails = userRepository.findByUsername(userName)
                        .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
                boolean isTokenValid = tokenRepository.findTokenByToken(refreshToken)
                        .map(t -> !t.isRevoked() && !t.isExpired() && t.isRefresh())
                        .orElse(false);
                if (jwtService.isValidToken(myUserDetails, refreshToken) && isTokenValid) {
                    //genarate new access token
                    String roles = myUserDetails.getRoles().stream()
                            .map(r -> r.getRole().getRoleName())
                            .collect(Collectors.toList())
                            .toString()
                            .replace("[", "")
                            .replace("]", "");
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("username", userName);
                    claims.put("roles", roles);
                    String accessToken = jwtService.genarateToken(claims, myUserDetails);

                    revokeToken(userName);

                    saveAccessToken(myUserDetails, accessToken);
                    new ObjectMapper().writeValue(response.getOutputStream(),
                            AuthenticationRespone.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(refreshToken)
                                    .userInfor(AuthenticationRespone.UserInfor.builder()
                                            .id(myUserDetails.getId())
                                            .username(myUserDetails.getUsername())
                                            .email(myUserDetails.getUsername())
                                            .avatar(myUserDetails.getAvtUrl())
                                            .fullName(myUserDetails.getFullName())
                                            .build()
                                    )
                                    .build()
                    );
                }
            }
        } catch (ExpiredJwtException e) {
            Token token = tokenRepository.findTokenByToken(refreshToken)
                    .orElseThrow(() -> new ResourceNotFoundExeption("Token is not found"));
            token.setExpired(true);
            tokenRepository.save(token);
        }
    }

    private void saveAccessToken(MyUser myUser, String accessToken) {
        Token tokenSaved = Token.builder()
                .myUser(myUser)
                .token(accessToken)
                .tokenType("BEARER")
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(tokenSaved);
    }

    private void saveRefreshToken(MyUser myUser, String refreshToken) {
        Token tokenSaved = Token.builder()
                .myUser(myUser)
                .token(refreshToken)
                .tokenType("BEARER")
                .expired(false)
                .revoked(false)
                .isRefresh(true)
                .build();
        tokenRepository.save(tokenSaved);
    }
}
