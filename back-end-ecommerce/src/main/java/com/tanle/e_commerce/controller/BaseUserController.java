package com.tanle.e_commerce.controller;

import com.tanle.e_commerce.entities.MyUser;
import com.tanle.e_commerce.respone.AuthenticationRespone;
import com.tanle.e_commerce.respone.MessageResponse;
import com.tanle.e_commerce.request.LoginRequest;
import com.tanle.e_commerce.service.TokenSerice;
import com.tanle.e_commerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public abstract class BaseUserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    protected TokenSerice tokenSerice;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<MessageResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        MessageResponse tokenMessage = tokenSerice.registerToken(request.getUsername());
        return new ResponseEntity<>(tokenMessage, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public MessageResponse logout(@AuthenticationPrincipal MyUser myUser) {
        if (myUser == null)
            return MessageResponse.builder()
                    .message("Unauthorized")
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        String username = myUser.getUsername();
        userService.updateLastAccess(username);
        tokenSerice.revokeToken(username);
        SecurityContextHolder.clearContext();

        return MessageResponse.builder()
                .status(HttpStatus.OK)
                .message("Logout successfully")
                .build();
    }

    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request
            , HttpServletResponse response) throws IOException {
        tokenSerice.refreshToken(request, response);
    }
}
