package com.tanle.e_commerce.exception;

public class ResourceExistedException  extends RuntimeException {
    public ResourceExistedException(String message) {
        super(message);
    }
}
