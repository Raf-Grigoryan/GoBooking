package org.example.gobookingcommon.customException;

public class UsersMismatchException extends RuntimeException {
    public UsersMismatchException(String message) {
        super(message);
    }
}
