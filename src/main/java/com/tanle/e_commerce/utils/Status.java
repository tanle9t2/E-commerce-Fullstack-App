package com.tanle.e_commerce.utils;

public enum Status {
    STOCK,
    OUTSTOCK;

    public static Status fromValues(String values) {
        for (Status status :  Status.values()) {
            if(String.valueOf(status).equalsIgnoreCase(values)) {
                return status;
            }
        }
        return null;
    }
}
