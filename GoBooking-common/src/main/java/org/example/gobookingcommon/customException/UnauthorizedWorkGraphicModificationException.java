package org.example.gobookingcommon.customException;

public class UnauthorizedWorkGraphicModificationException extends RuntimeException {
    public UnauthorizedWorkGraphicModificationException(String message) {
        super(message);
    }
}
