package com.tanle.e_commerce.service.authorization;

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

import java.util.Map;
import java.util.function.Supplier;

@Component
public class MyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    @Qualifier("ownerService")
    private Map<String, OwnerService<?, Integer>> ownershipServices;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();
        String username = authentication.get().getName();  // Get the authenticated user
        // Example: Path might look like /api/v1/orders/{orderId}
        String[] pathSplit = request.getRequestURI().split("/");
        String entityType = pathSplit[pathSplit.length-2];
        Integer entityId = Integer.parseInt(pathSplit[pathSplit.length-1]);
        // Find the correct OwnershipService based on the entity type
        OwnerService<?, Integer> ownershipService = getOwnershipService(entityType);

        // Check if the user owns the entity
        boolean userOwnsEntity = ownershipService.userOwnEntity(entityId, username);
        return new AuthorizationDecision(userOwnsEntity);
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
