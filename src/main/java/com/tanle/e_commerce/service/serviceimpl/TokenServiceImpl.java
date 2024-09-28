package com.tanle.e_commerce.service.serviceimpl;

import com.tanle.e_commerce.Repository.Jpa.TokenRepository;
import com.tanle.e_commerce.Repository.Jpa.UserRepository;
import com.tanle.e_commerce.entities.Token;
import com.tanle.e_commerce.entities.User;
import com.tanle.e_commerce.exception.ResourceNotFoundExeption;
import com.tanle.e_commerce.payload.MessageResponse;
import com.tanle.e_commerce.service.JwtService;
import com.tanle.e_commerce.service.TokenSerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TokenServiceImpl implements TokenSerice {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional
    public MessageResponse registerToken(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExeption("Not found user"));
        Map<String, Object> claims = new HashMap<>();
        claims.put("username",user.getUsername());
        claims.put("roles",user.getRoles().toString());
        String token = jwtService.gernarateKey(claims,user);

        saveToken(user,token);
        return MessageResponse.builder()
                .data(token)
                .message("Regist token successfully")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public String authenticate(Map<String, String> request) {
        return null;
    }

    private void saveToken(User myUser, String token) {
        Token tokenSaved = Token.builder()
                .myUser(myUser)
                .token(token)
                .tokenType("BEARER")
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(tokenSaved);
    }
}
