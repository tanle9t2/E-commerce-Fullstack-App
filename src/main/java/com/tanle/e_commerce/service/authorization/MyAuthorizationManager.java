package com.tanle.e_commerce.service.authorization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanle.e_commerce.entities.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class MyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    @Qualifier("ownerService")
    private Map<String, OwnerService<?, Integer>> ownershipServices;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();
        String entityType;
        int entityId;
        String[] pathSplit = request.getRequestURI().split("/");
        String username = authentication.get().getName();  // Get the authenticated user
        if (request.getRequestURI().matches("^/api/([a-zA-Z0-9]+)/([a-zA-Z]+)/(\\d+)$")) {
            // Example: Path might look like /api/v1/orders/{orderId}
            entityType = pathSplit[pathSplit.length - 2];
            entityId = Integer.parseInt(pathSplit[pathSplit.length - 1]);
        } else {
            //Example: Path: /api/v1/user/address
            String body = getRequestBody(request);
            entityType = pathSplit[pathSplit.length - 2];
            entityId = extractEntityIdFromRequestBody(body);
        }
        // Find the correct OwnershipService based on the entity type
        OwnerService<?, Integer> ownershipService = getOwnershipService(entityType);
        // Check if the user owns the entity
        boolean userOwnsEntity = ownershipService.userOwnEntity(entityId, username);
        return new AuthorizationDecision(userOwnsEntity);
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining());
        }catch (Exception e){
            e.printStackTrace();
            return "{}";
        }
    }

    private Integer extractEntityIdFromRequestBody(String requestBody) {
        // Assuming the request body is a JSON object with an "id" field
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            return jsonNode.get("userIdRequest").asInt(); // Extract and return the "id" field
        } catch (IOException e) {
            e.printStackTrace(); // Handle JSON parsing error
        }
        return null; // Return null if the extraction fails
    }

    private OwnerService<?, Integer> getOwnershipService(String entityType) {
        return switch (entityType) {
            case "order" -> ownershipServices.get("order");
            case "user" -> ownershipServices.get("user");
            case "product" -> ownershipServices.get("product");
            case "tenant" -> ownershipServices.get("tenant");
            default -> throw new RuntimeException("No OwnerService found for entity type: " + entityType);
        };

    }
}
