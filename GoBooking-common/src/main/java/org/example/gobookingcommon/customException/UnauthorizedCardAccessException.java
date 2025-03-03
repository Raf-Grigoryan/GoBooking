package org.example.gobookingcommon.customException;

public class UnauthorizedCardAccessException extends RuntimeException {
    public UnauthorizedCardAccessException(String message) {
        super(message);
    }
}
