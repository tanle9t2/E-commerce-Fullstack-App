package com.tanle.e_commerce.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomAccessDeniedExceptionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response
            , AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Customize the response content, e.g., JSON error message
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Create a custom message to return
        String jsonResponse = "{\"error\": \"Access Denied\"," +
                " \"message\": \"You do not have permission to access this resource.\"}";
        response.getWriter().write(jsonResponse);
    }
}
