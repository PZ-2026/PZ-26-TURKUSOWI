package com.turkusowi.backendapi.common;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
