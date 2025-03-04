package org.example.gobookingcommon.customException;

public class TypeNotExistException extends RuntimeException {
    public TypeNotExistException(String message) {
        super(message);
    }
}
