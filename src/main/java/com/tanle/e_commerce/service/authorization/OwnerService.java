package com.tanle.e_commerce.service.authorization;

public interface OwnerService <T,ID>{
    boolean userOwnEntity(ID id, String username);
}
