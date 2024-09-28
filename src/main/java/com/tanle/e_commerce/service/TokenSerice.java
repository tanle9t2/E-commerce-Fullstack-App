package com.tanle.e_commerce.service;

import com.tanle.e_commerce.entities.User;
import com.tanle.e_commerce.payload.MessageResponse;

import java.util.Map;

public interface TokenSerice {
    MessageResponse registerToken(int userId);
    String authenticate(Map<String, String> request);
}
