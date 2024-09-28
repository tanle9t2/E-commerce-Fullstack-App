package com.tanle.e_commerce.service;

import com.tanle.e_commerce.entities.Token;
import com.tanle.e_commerce.entities.User;
import com.tanle.e_commerce.payload.MessageResponse;

import java.util.Map;

public interface TokenSerice {
    MessageResponse registerToken(String username);
    String authenticate(Map<String, String> request);

    Token findToken(String jwtToken);

    MessageResponse revokeToken(String username);
}
