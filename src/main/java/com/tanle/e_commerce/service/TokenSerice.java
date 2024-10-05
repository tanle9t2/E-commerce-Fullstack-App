package com.tanle.e_commerce.service;

import com.tanle.e_commerce.entities.Token;
import com.tanle.e_commerce.respone.AuthenticationRespone;
import com.tanle.e_commerce.respone.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public interface TokenSerice {
    MessageResponse registerToken(String username);
    String authenticate(Map<String, String> request);

    Token findToken(String jwtToken);

    MessageResponse revokeToken(String username);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
