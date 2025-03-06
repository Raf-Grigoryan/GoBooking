package org.example.gobookingcommon.customException;

public class DirectorNotMatchException extends RuntimeException {
    public DirectorNotMatchException(String message) {
        super(message);
    }
}
