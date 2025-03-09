package org.example.gobookingcommon.customException;

public class UnauthorizedServiceDeletionException extends RuntimeException {
    public UnauthorizedServiceDeletionException(String message) {
        super(message);
    }
}
