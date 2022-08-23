package com.resell.resell.exception.user;

public class UnauthenticatedUserException extends RuntimeException {
    public UnauthenticatedUserException(String message) {
        super(message);
    }
}
